package reactor.types;

import com.ib.client.Bar;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@Data
public class IbBar {

    public static final IbBar COMPLETE = new IbBar();

    private static final DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder().appendPattern("yyyyMMdd[  HH:mm:ss]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private final LocalDateTime time;
    private final BigDecimal open;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal close;
    private final long volume;
    private final int count;
    private final BigDecimal wap;

    public IbBar(Bar bar) {
        time = LocalDateTime.from(dateTimeFormatter.parse(bar.time()));
        open = BigDecimal.valueOf(bar.open());
        high = BigDecimal.valueOf(bar.high());
        low = BigDecimal.valueOf(bar.low());
        close = BigDecimal.valueOf(bar.close());
        volume = bar.volume();
        count = bar.count();
        wap = BigDecimal.valueOf(bar.wap());
    }

    private IbBar() {
        time = null;
        open = null;
        high = null;
        low = null;
        close = null;
        volume = 0;
        count = 0;
        wap = null;
    }
}
