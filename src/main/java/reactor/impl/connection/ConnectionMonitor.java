package reactor.impl.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ConnectionMonitor implements AutoCloseable {

    private static final int CLOSE_TIMEOUT_MS = 10_000;
    private static final int SLEEP_DELAY_MS = 100;
    private static final Logger log = LoggerFactory.getLogger(ConnectionMonitor.class);
    private final Lock statusLock = new ReentrantLock();
    private final Condition statusCondition = statusLock.newCondition();
    private final SleepTimer timer = new SleepTimer();
    private final Duration connectionDelay;
    private boolean isConnected;
    private Status expectedStatus;
    private AtomicReference<Status> status;
    private AtomicReference<Command> command;
    private Thread thread;

    public ConnectionMonitor(Duration connectionDelay) {
        this.connectionDelay = connectionDelay;
    }

    protected abstract void connectRequest();

    protected abstract void disconnectRequest(boolean reconnect);

    protected abstract void afterConnect();

    protected abstract void onConnectStatusChange(Boolean connected);

    public void start() {
        status = new AtomicReference<>(Status.UNKNOWN);
        command = new AtomicReference<>(Command.NONE);
        isConnected = false;

        thread = new Thread(this::run);
        thread.setName("Connection monitor");
        thread.start();

        try {
            waitForStatus(Status.DISCONNECTED, 1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("Too long expectation for DISCONNECT status");
        }
    }

    @Override
    public void close() {
        if (status.get() != Status.DISCONNECTED) {
            disconnect();
        }
        waitForStatus(Status.DISCONNECTED);

        thread.interrupt();
        try {
            thread.join(CLOSE_TIMEOUT_MS);
        } catch (InterruptedException e) {
            log.error("Current thread '{}' has been interrupted at shutdown", this);
        }
    }

    public Status status() {
        return status.get();
    }

    public synchronized void connect() {
        setCommand(Command.CONNECT);
    }

    public synchronized void confirmConnection() {
        setCommand(Command.CONFIRM_CONNECT);
    }

    public synchronized void reconnect() {
        setCommand(Command.RECONNECT);
    }

    public void disconnect() {
        if (!thread.isAlive()) {
            throw new IllegalStateException("Thread has not been started");
        }

        setCommand(Command.DISCONNECT);
    }

    private void run() {
        try {
            setStatus(Status.DISCONNECTED);

            while (!Thread.interrupted()) {
                Command commandLocal = command.getAndSet(Command.NONE);
                commandLocal = handleCommand(commandLocal);

                if (commandLocal == Command.NONE) {
                    Thread.sleep(SLEEP_DELAY_MS);
                }
            }
        } catch (InterruptedException ignored) {
            // ignored
        } catch (Exception e) {
            log.error("Exception in Connection Monitor: {}", e.getMessage(), e);
        }
    }

    private Command handleCommand(Command newCommand) {
        switch (newCommand) {

            case CONNECT:
                setStatus(Status.CONNECTING);
                connectRequest();
                break;

            case RECONNECT:
                setStatus(Status.DISCONNECTING);
                disconnectRequest(true);
                setStatus(Status.DISCONNECTED);

                timer.start(connectionDelay.toMillis(), () -> {
                    setStatus(Status.CONNECTING);
                    connectRequest();
                });

                break;

            case CONFIRM_CONNECT:
                /*
                 * Right after connect, an error 507 (Bad Message Length) can occur, so we re-read
                 * command to be sure valid connection still persist
                 */
                timer.start(connectionDelay.toMillis(), () -> {
                    setStatus(Status.CONNECTED);
                    afterConnect();
                });

                break;

            case DISCONNECT:
                setStatus(Status.DISCONNECTING);
                disconnectRequest(false);
                setStatus(Status.DISCONNECTED);
                break;

            case NONE:
                if (timer.isRunning()) {
                    setStatus(Status.SLEEP);
                    if (timer.run()) {
                        timer.reset();
                    }
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + newCommand);
        }

        return newCommand;
    }

    private void setCommand(Command newCommand) {
        timer.reset();
        command.set(newCommand);
        log.debug("Command: {}", newCommand.name());
    }

    private void setStatus(Status newStatus) {
        Status oldStatus = status.getAndSet(newStatus);
        if (oldStatus == newStatus) {
            return;
        }

        log.debug("Status change: {} => {}", oldStatus.name(), newStatus.name());
        triggerStatusOnChange(newStatus);

        statusLock.lock();
        try {
            if (expectedStatus == newStatus) {
                statusCondition.signalAll();
            }
        } finally {
            statusLock.unlock();
        }
    }

    private void triggerStatusOnChange(Status newStatus) {
        if (!isConnected && newStatus == Status.CONNECTED) {
            isConnected = true;
            onConnectStatusChange(true);
        } else if (isConnected && newStatus != Status.CONNECTED) {
            isConnected = false;
            onConnectStatusChange(false);
        }
    }

    private void waitForStatus(Status newStatus, long time, TimeUnit unit) throws TimeoutException {
        statusLock.lock();
        try {
            expectedStatus = newStatus;
            while (status.get() != expectedStatus) {
                if (!statusCondition.await(time, unit)) {
                    throw new TimeoutException(String.format("Timeout of '%s' status. Actual status is '%s'",
                        expectedStatus,
                        status
                    ));
                }
            }
        } catch (InterruptedException e) {
            log.error("waitForStatus has been interrupted", e);
        } finally {
            statusLock.unlock();
        }
    }

    private void waitForStatus(Status newStatus) {
        statusLock.lock();
        try {
            expectedStatus = newStatus;
            while (status.get() != expectedStatus) {
                statusCondition.await();
            }
        } catch (InterruptedException e) {
            log.warn("waitForStatus has been interrupted", e);
        } finally {
            statusLock.unlock();
        }
    }

    public enum Command {
        NONE,
        DISCONNECT,
        CONNECT,
        RECONNECT,
        CONFIRM_CONNECT
    }

    public enum Status {
        UNKNOWN,
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        SLEEP,
    }
}
