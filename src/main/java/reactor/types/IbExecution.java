package reactor.types;

import com.ib.client.Execution;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;

@Data
public class IbExecution {
    private static final DateTimeFormatter dateTimeFormatter =
        DateTimeFormat.forPattern("yyyyMMdd  HH:mm:ss")
            .withZone(DateTimeZone.forID("America/New_York"));

    private final int orderId;
    private final int clientId;
    private final String execId;
    private final long time;
    private final String acctNumber;
    private final String exchange;
    private final String side;
    private final BigDecimal shares;
    private final BigDecimal price;
    private final int permId;
    private final int liquidation;
    private final BigDecimal cumQty;
    private final BigDecimal avgPrice;
    private final String orderRef;
    private final String evRule;
    private final BigDecimal evMultiplier;
    private final String modelCode;

    public IbExecution(Execution base) {
        orderId = base.orderId();
        clientId = base.clientId();
        execId = base.execId();
        time = DateTime.parse(base.time(), dateTimeFormatter).toInstant().getMillis();
        acctNumber = base.acctNumber();
        exchange = base.exchange();
        side = base.side();
        shares = BigDecimal.valueOf(base.shares());
        price = BigDecimal.valueOf(base.price());
        permId = base.permId();
        liquidation = base.liquidation();
        cumQty = BigDecimal.valueOf(base.cumQty());
        avgPrice = BigDecimal.valueOf(base.avgPrice());
        orderRef = base.orderRef();
        evRule = base.evRule();
        evMultiplier = BigDecimal.valueOf(base.evMultiplier());
        modelCode = base.modelCode();
    }

}
