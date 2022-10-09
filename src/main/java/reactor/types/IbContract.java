package reactor.types;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.DeltaNeutralContract;
import com.ib.client.Types;
import lombok.Data;
import reactor.impl.utils.Converter;

import java.math.BigDecimal;
import java.util.List;

@Data
public class IbContract {
    private final int conid;
    private final String symbol;
    private final Types.SecType secType;
    private final String lastTradeDateOrContractMonth;
    private final BigDecimal strike;
    private final Types.Right right;
    private final String multiplier;
    private final String exchange;
    private final String primaryExchange;
    private final String currency;
    private final String localSymbol;
    private final String tradingClass;
    private final Types.SecIdType secIdType;
    private final String secId;
    private final DeltaNeutralContract deltaNeutralContract;
    private final boolean includeExpired;
    private final String comboLegsDescription;
    private final List<ComboLeg> comboLegs;

    public IbContract(Contract base) {
        conid = base.conid();
        symbol = base.symbol();
        secType = base.secType();
        lastTradeDateOrContractMonth = base.lastTradeDateOrContractMonth();
        strike = Converter.doubleToBigDecimal("strike", base.strike());
        right = base.right();
        multiplier = base.multiplier();
        exchange = base.exchange();
        primaryExchange = base.primaryExch();
        currency = base.currency();
        localSymbol = base.localSymbol();
        tradingClass = base.tradingClass();
        secIdType = base.secIdType();
        secId = base.secId();
        deltaNeutralContract = base.deltaNeutralContract();
        includeExpired = base.includeExpired();
        comboLegsDescription = base.comboLegsDescrip();
        comboLegs = base.comboLegs();
    }
}
