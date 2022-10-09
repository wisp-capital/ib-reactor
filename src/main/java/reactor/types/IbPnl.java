package reactor.types;

import lombok.Data;

import java.math.BigDecimal;

/**
 * PnL structure.
 *
 * @implNote Any of BigDecimal members can be null.
 */
@Data
public class IbPnl {
    private final Integer position;
    private final BigDecimal dailyPnL;
    private final BigDecimal unrealizedPnL;
    private final BigDecimal realizedPnL;
    private final BigDecimal value;
}
