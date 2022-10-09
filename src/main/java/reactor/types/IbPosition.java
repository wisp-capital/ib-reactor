package reactor.types;

import com.ib.client.Contract;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ Data class for IB position data.
 */
@Data
public class IbPosition {

    public static final IbPosition COMPLETE = new IbPosition();

    private final String account;
    private final Contract contract;
    private final BigDecimal pos;
    private final BigDecimal avgCost;

    public IbPosition(String account, Contract contract, BigDecimal pos, BigDecimal avgCost) {
        this.account = account;
        this.contract = contract;
        this.pos = pos;
        this.avgCost = avgCost;
    }

    private IbPosition() {
        account = null;
        contract = null;
        pos = BigDecimal.ZERO;
        avgCost = BigDecimal.ZERO;
    }
}
