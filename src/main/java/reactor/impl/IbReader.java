package reactor.impl;

import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IbReader {

    private static final Logger log = LoggerFactory.getLogger(IbReader.class);

    private static final int STOP_TIMEOUT_MS = 1000;
    private static final int WAIT_TIMEOUT_MS = 100;
    private final EJavaSignal signal;
    private final EClientSocket socket;
    private EReader reader;
    private final Thread thread = new Thread(this::processMessages);

    public IbReader(EClientSocket socket, EJavaSignal signal) {
        this.socket = socket;
        this.signal = signal;

        thread.setName("IbReader");
        thread.setPriority(Thread.MAX_PRIORITY);
    }

    private static void stopThread(Thread threadToStop) {
        try {
            threadToStop.join(STOP_TIMEOUT_MS);
        } catch (InterruptedException e) {
            return;
        }
        if (threadToStop.isAlive()) {
            log.warn("Fail to shutdown thread '{}'", threadToStop.getName());
        }
    }

    public void start() {
        reader = new EReader(socket, signal);
        reader.setPriority(Thread.MAX_PRIORITY);

        reader.start();
        thread.start();
    }

    public synchronized void close() {
        thread.interrupt();
        if (reader != null) {
            reader.interrupt();
        }

        stopThread(thread);
        if (reader != null) {
            stopThread(reader);
        }
    }

    private void processMessages() {
        while (!Thread.interrupted()) {
            if (socket.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    log.error("Reader error", e);
                }
            } else {
                try {
                    Thread.sleep(WAIT_TIMEOUT_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
