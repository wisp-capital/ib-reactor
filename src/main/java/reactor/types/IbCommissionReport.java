package reactor.types;

import com.ib.client.CommissionReport;
import lombok.Data;
import reactor.impl.utils.Converter;

import java.math.BigDecimal;

@Data
public class IbCommissionReport {

    private final String execId;
    private final BigDecimal commission;
    private final String currency;
    private final BigDecimal realizedPnl;
    private final BigDecimal yield;
    private final int yieldRedemptionDate;

    public IbCommissionReport(CommissionReport base) {
        execId = base.execId();
        commission = Converter.doubleToBigDecimal("commission", base.commission());
        currency = base.currency();
        realizedPnl = Converter.doubleToBigDecimal("realizedPNL", base.realizedPNL());
        yield = Converter.doubleToBigDecimal("yield", base.yield());
        yieldRedemptionDate = base.yieldRedemptionDate();
    }
}
