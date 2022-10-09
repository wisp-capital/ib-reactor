package reactor.impl.cache;

import lombok.Data;

@Data
class PositionKey {
    private final String account;
    private final int contractId;
}
