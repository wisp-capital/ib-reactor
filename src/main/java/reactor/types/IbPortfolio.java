package reactor.types;

import com.ib.client.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class IbPortfolio {
    public static final IbPortfolio COMPLETE = new IbPortfolio();

    private final Contract contract;
    private final BigDecimal position;
    private final BigDecimal marketPrice;
    private final BigDecimal marketValue;
    private final BigDecimal averageCost;
    private final BigDecimal unrealizedPNL;
    private final BigDecimal realizedPNL;
    private final String account;

    private IbPortfolio() {
        contract = null;
        position = null;
        marketPrice = null;
        marketValue = null;
        averageCost = null;
        unrealizedPNL = null;
        realizedPNL = null;
        account = null;
    }

}
