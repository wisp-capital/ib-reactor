package reactor.impl.request;

import java.util.Objects;

public class RequestKey {
    private final RequestRepository.Type type;
    private final Integer id;

    RequestKey(RequestRepository.Type type, Integer id) {
        this.type = type;
        this.id = id;
    }

    final RequestRepository.Type getType() {
        return type;
    }

    public final Integer getId() {
        return id;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        RequestKey typeKey = (RequestKey) obj;
        return (type == null || type == typeKey.type) && Objects.equals(id, typeKey.id);
    }

    @Override
    public final int hashCode() {
        return 0;
    }

    @Override
    public final String toString() {
        if (id == null && type == null) {
            return "Invalid message";
        }

        if (id != null && type == null) {
            return id.toString();
        }

        if (id == null) {
            return type.name();
        }

        return String.format("%s,%s", type.name(), id);
    }
}
