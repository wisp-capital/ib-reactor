package reactor.impl.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.IbClient;
import reactor.IbExceptions;
import reactor.core.publisher.Flux;
import reactor.impl.IdGenerator;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RequestRepository implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(RequestRepository.class);
    private final IbClient client;
    private final IdGenerator idGenerator;

    @SuppressWarnings("rawtypes")
    private final Map<RequestKey, Request> requests = new ConcurrentHashMap<>();

    public RequestRepository(IbClient client, IdGenerator idGenerator) {
        this.client = client;
        this.idGenerator = idGenerator;
    }

    @Override
    public final void close() {
        requests.values().forEach(Request::unregister);
        requests.clear();
        log.debug("RequestRepository is closed");
    }

    public final <T> RequestBuilder<T> builder() {
        return new RequestBuilder<>();
    }

    public final <T> void onNext(Type type, Integer reqId, T data, Boolean shouldExists) {
        get(type, reqId, shouldExists).ifPresent(request -> request.onNext(data));
    }

    public final void onError(Type type, Integer reqId, Throwable throwable, Boolean shouldExists) {
        get(type, reqId, shouldExists).ifPresent(request -> request.onError(throwable));
    }

    public final void onError(Type type, Integer reqId, Throwable throwable) {
        onError(type, reqId, throwable, true);
    }

    public final void onError(Integer reqId, Throwable throwable) {
        get(null, reqId, true).ifPresent(request -> request.onError(throwable));
    }

    public final void onError(Integer reqId, Throwable throwable, Boolean shouldExists) {
        get(null, reqId, shouldExists).ifPresent(request -> request.onError(throwable));
    }

    public final void onComplete(Type type, Integer reqId, Boolean shouldExists) {
        get(type, reqId, shouldExists).ifPresent(Request::onComplete);
    }

    public final <T> void onNextAndComplete(
        Type type, Integer reqId, T data, Boolean shouldExists
    ) {
        get(type, reqId, shouldExists).ifPresent(request -> {
            request.onNext(data);
            request.onComplete();
        });
    }

    public final Object getUserData(Type type, int reqId) {
        return get(type, reqId, true).map(Request::getUserData).orElse(null);
    }

    private <T> Optional<Request<T>> get(Type type, Integer reqId, Boolean shouldExists) {
        @SuppressWarnings("unchecked") Request<T> request =
            requests.get(new RequestKey(type, reqId));
        if (request == null) {
            if (shouldExists) {
                log.error("Cannot find request '{}' id={}", type, reqId);
            } else {
                log.trace("Cannot find request '{}' id={}", type, reqId);
            }
        }
        return Optional.ofNullable(request);
    }

    private void remove(RequestKey key) {
        Request<?> prev = requests.remove(key);
        if (prev == null) {
            throw new IllegalArgumentException(String.format("Unknown request: %s", key));
        }
    }

    public enum Type {
        EVENT_CONTRACT_PNL,
        EVENT_ACCOUNT_PNL,
        EVENT_POSITION,
        EVENT_POSITION_MULTI,
        EVENT_ORDER_STATUS,
        EVENT_MARKET_DATA,
        EVENT_MARKET_DATA_LVL2,
        EVENT_PORTFOLIO,
        EVENT_HISTORICAL_DATA,
        EVENT_EXECUTION_INFO,
        REQ_MARKET_DATA,
        REQ_MARKET_DEPTH_EXCHANGES,
        REQ_CURRENT_TIME,
        REQ_ORDER_PLACE,
        REQ_ORDER_CANCEL,
        REQ_ORDER_LIST,
        REQ_EXECUTIONS,
        REQ_CONTRACT_DETAIL,
        REQ_CONTRACT_DESCRIPTION,
        REQ_HISTORICAL_MIDPOINT_TICK,
        REQ_HISTORICAL_BID_ASK_TICK,
        REQ_HISTORICAL_TRADE,
        REQ_HISTORICAL_DATA,
        REQ_ACCOUNT_SUMMARY,
        REQ_MARKET_RULE,
    }

    public class RequestBuilder<T> {
        private RequestRepository.Type type;
        private Consumer<Integer> register;
        private Consumer<Integer> unregister;
        private Object userData;
        private boolean withId;
        private Integer id;

        public final RequestBuilder<T> type(RequestRepository.Type newType) {
            type = newType;
            return this;
        }

        public final RequestBuilder<T> register(Consumer<Integer> newRegister) {
            register = newRegister;
            withId = true;
            return this;
        }

        public final RequestBuilder<T> register(Runnable newRegister) {
            register = unused -> newRegister.run();
            return this;
        }

        public final RequestBuilder<T> register(int newId, Runnable newRegister) {
            id = newId;
            register = unused -> newRegister.run();
            return this;
        }

        public final RequestBuilder<T> unregister(Consumer<Integer> newUnregister) {
            unregister = newUnregister;
            withId = true;
            return this;
        }

        public final RequestBuilder<T> unregister(Runnable newUnregister) {
            unregister = unused -> newUnregister.run();
            return this;
        }

        public final RequestBuilder<T> userData(Object data) {
            userData = data;
            return this;
        }

        public final RequestBuilder<T> id(int newId) {
            id = newId;
            withId = true;
            return this;
        }

        public final Flux<T> subscribe() {

            return Flux.create(emitter -> {
                if (register == null) {
                    emitter.error(new IllegalArgumentException("Registration function is mandatory"));
                    return;
                }
                if (type == null) {
                    emitter.error(new IllegalArgumentException("Request type is mandatory"));
                    return;
                }

                Integer requestId;

                if (withId && id == null) {
                    requestId = idGenerator.nextId();
                } else {
                    requestId = id;
                }

                RequestKey key = new RequestKey(type, requestId);
                Request<T> request = new Request<>(emitter, key, register, unregister, userData);

                if (!client.isConnected()) {
                    emitter.error(new IbExceptions.NotConnectedError());
                    return;
                }

                Request<?> old = requests.putIfAbsent(key, request);
                if (old != null) {
                    log.error("Duplicated request: {} {}", key, request);
                    log.error("This seems to mean that we are subscribed twice to the same thing " +
                        "on the same client");
                    log.error("Existing requests are {}", requests);
                    emitter.error(new IbExceptions.DuplicatedRequestError(key));
                    return;
                }

                emitter.onDispose(() -> {
                    remove(key);
                    if (client.isConnected()) {
                        log.debug("Unregister from {}", request);
                        request.unregister();
                    } else {
                        log.debug("Have no connection at unregister of {}", key);
                    }
                });

                request.register();
                log.info("Register to {}", request);
            });
        }
    }
}
