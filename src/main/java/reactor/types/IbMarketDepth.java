package reactor.types;

import com.ib.client.Contract;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IbMarketDepth {

    private final Contract contract;
    private final Integer position;
    private final Side side;
    private final BigDecimal price;
    private final Integer size;
    private final String marketMaker;

    public IbMarketDepth(
        Contract contract,
        Integer position,
        Integer side,
        BigDecimal price,
        Integer size,
        String marketMaker
    ) {
        switch (side) {
            case 0 -> this.side = Side.SELL;
            case 1 -> this.side = Side.BUY;
            default ->
                throw new IllegalArgumentException(String.format("Unexpected side: %d", side));
        }

        this.contract = contract;
        this.position = position;
        this.price = price;
        this.size = size;
        this.marketMaker = marketMaker;
    }

    public Key key() {
        return new Key(side, position);
    }

    public enum Side {
        SELL,
        BUY
    }

    public enum Operation {
        INSERT,
        UPDATE,
        REMOVE
    }

    @Data
    public static class Key {
        private final Side side;
        private final Integer position;
    }
}
