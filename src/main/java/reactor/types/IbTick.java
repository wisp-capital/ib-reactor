package reactor.types;

import com.ib.client.TickAttrib;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class IbTick {

    private static final Logger log = LoggerFactory.getLogger(IbTick.class);
    private Integer bidSize;
    private Integer askSize;
    private Integer lastSize;
    private Integer volume;
    private Integer volumeAverage;
    private Integer optionCallOpenInterest;
    private Integer optionPutOpenInterest;
    private Integer optionCallVolume;
    private Integer optionPutVolume;
    private Integer actionVolume;
    private Integer actionImbalance;
    private Integer regulatoryImbalance;
    private Integer shortTermVolume3Min;
    private Integer shortTermVolume5Min;
    private Integer shortTermVolume10Min;
    private Integer delayedBidSize;
    private Integer delayedAskSize;
    private Integer delayedLastSize;
    private Integer delayedVolume;
    private Integer futuresOpenInterest;
    private Integer averageOptionVolume;
    private Integer shortableShares;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal lastPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal openTick;
    private BigDecimal low13Weeks;
    private BigDecimal high13Weeks;
    private BigDecimal low26Weeks;
    private BigDecimal high26Weeks;
    private BigDecimal low52Weeks;
    private BigDecimal high52Weeks;
    private BigDecimal auctionPrice;
    private BigDecimal markPrice;
    private BigDecimal bidYield;
    private BigDecimal askYield;
    private BigDecimal lastYield;
    private BigDecimal lastRthTrade;
    private BigDecimal delayedBid;
    private BigDecimal delayedAsk;
    private BigDecimal delayedLast;
    private BigDecimal delayedHighPrice;
    private BigDecimal delayedLowPrice;
    private BigDecimal delayedClose;
    private BigDecimal delayedOpen;
    private BigDecimal creditmanMarkPrice;
    private BigDecimal creditmanSlowMarkPrice;
    private BigDecimal delayedBidOption;
    private BigDecimal delayedAskOption;
    private BigDecimal delayedLastOption;
    private BigDecimal delayedModelOption;
    private String bidExchange;
    private String askExchange;
    private String lastTimestamp;
    private String rtVolume;
    private String ibDividends;
    private String news;
    private String rtTradeVolume;
    private String lastExchange;
    private String lastRegulatoryTime;
    private String delayedLastTimestamp;
    private BigDecimal optionHistoricalVolatility;
    private BigDecimal optionImpliedVolatility;
    private BigDecimal indexFuturePremium;
    private BigDecimal shortable;
    private BigDecimal halted;
    private BigDecimal tradeCount;
    private BigDecimal tradeRate;
    private BigDecimal volumeRate;
    private BigDecimal rtHistoricalVolatility;
    private LocalDateTime updateTime;

    public void setIntValue(int tickerId, Integer type, Integer value) {

        Types.valueOf(type).ifPresent(tickType -> {
            switch (tickType) {
                case BID_SIZE -> bidSize = value;
                case ASK_SIZE -> askSize = value;
                case LAST_SIZE -> lastSize = value;
                case VOLUME -> volume = value;
                case VOLUME_AVERAGE -> volumeAverage = value;
                case OPTION_CALL_OPEN_INTEREST -> optionCallOpenInterest = value;
                case OPTION_PUT_OPEN_INTEREST -> optionPutOpenInterest = value;
                case OPTION_CALL_VOLUME -> optionCallVolume = value;
                case OPTION_PUT_VOLUME -> optionPutVolume = value;
                case ACTION_VOLUME -> actionVolume = value;
                case ACTION_IMBALANCE -> actionImbalance = value;
                case REGULATORY_IMBALANCE -> regulatoryImbalance = value;
                case SHORT_TERM_VOLUME3_MIN -> shortTermVolume3Min = value;
                case SHORT_TERM_VOLUME5_MIN -> shortTermVolume5Min = value;
                case SHORT_TERM_VOLUME10_MIN -> shortTermVolume10Min = value;
                case DELAYED_BID_SIZE -> delayedBidSize = value;
                case DELAYED_ASK_SIZE -> delayedAskSize = value;
                case DELAYED_LAST_SIZE -> delayedLastSize = value;
                case DELAYED_VOLUME -> delayedVolume = value;
                case FUTURES_OPEN_INTEREST -> futuresOpenInterest = value;
                case AVERAGE_OPTION_VOLUME -> averageOptionVolume = value;
                case SHORTABLE_SHARES -> shortableShares = value;
                default -> log.warn("Unknown int type for tick, type={}, value={}", type, value);
            }
            log.trace("Set value for {}: {} = {}", tickerId, tickType, value);
        });
    }

    public void setPriceValue(int tickerId, Integer type, BigDecimal value, TickAttrib attrib) {
        Types.valueOf(type).ifPresent(tickType -> {
            switch (tickType) {
                case BID -> bid = value;
                case ASK -> ask = value;
                case LAST -> lastPrice = value;
                case HIGH -> highPrice = value;
                case LOW -> lowPrice = value;
                case CLOSE_PRICE -> closePrice = value;
                case OPEN_TICK -> openTick = value;
                case LOW13_WEEKS -> low13Weeks = value;
                case HIGH13_WEEKS -> high13Weeks = value;
                case LOW26_WEEKS -> low26Weeks = value;
                case HIGH26_WEEKS -> high26Weeks = value;
                case LOW52_WEEKS -> low52Weeks = value;
                case HIGH52_WEEKS -> high52Weeks = value;
                case AUCTION_PRICE -> auctionPrice = value;
                case MARK_PRICE -> markPrice = value;
                case BID_YIELD -> bidYield = value;
                case ASK_YIELD -> askYield = value;
                case LAST_YIELD -> lastYield = value;
                case LAST_RTHTRADE -> lastRthTrade = value;
                case DELAYED_BID -> delayedBid = value;
                case DELAYED_ASK -> delayedAsk = value;
                case DELAYED_LAST -> delayedLast = value;
                case DELAYED_HIGH_PRICE -> delayedHighPrice = value;
                case DELAYED_LOW_PRICE -> delayedLowPrice = value;
                case DELAYED_CLOSE -> delayedClose = value;
                case DELAYED_OPEN -> delayedOpen = value;
                case CREDITMAN_MARK_PRICE -> creditmanMarkPrice = value;
                case CREDITMAN_SLOW_MAR_KPRICE -> creditmanSlowMarkPrice = value;
                case DELAYED_BID_OPTION -> delayedBidOption = value;
                case DELAYED_ASK_OPTION -> delayedAskOption = value;
                case DELAYED_LAST_OPTION -> delayedLastOption = value;
                case DELAYED_MODEL_OPTION -> delayedModelOption = value;
                default -> log.warn("Unknown price type for tick, type={}, value={}", type, value);
            }
            log.trace(
                "Set value for {}: {} = {}, attr: [auto exec: {}, past limit: {}, pre open: {}]",
                tickerId,
                tickType,
                value,
                attrib.canAutoExecute(),
                attrib.pastLimit(),
                attrib.preOpen()
            );
        });
    }

    public void setStringValue(int tickerId, Integer type, String value) {
        Types.valueOf(type).ifPresent(tickType -> {
            switch (tickType) {
                case BID_EXCHANGE -> bidExchange = value;
                case ASK_EXCHANGE -> askExchange = value;
                case LAST_TIMESTAMP -> lastTimestamp = value;
                case RT_VOLUME -> rtVolume = value;
                case IB_DIVIDENDS -> ibDividends = value;
                case NEWS -> news = value;
                case RT_TRADE_VOLUME -> rtTradeVolume = value;
                case LAST_EXCHANGE -> lastExchange = value;
                case LAST_REGULATORY_TIME -> lastRegulatoryTime = value;
                case DELAYED_LAST_TIMESTAMP -> delayedLastTimestamp = value;
                default -> log.warn("Unknown string type for tick, type={}, value={}", type, value);
            }
            log.trace("Set value for {}: {} = {}", tickerId, tickType, value);
        });
    }

    public void setGenericValue(int tickerId, Integer type, BigDecimal value) {
        Types.valueOf(type).ifPresent(tickType -> {
            switch (tickType) {
                case OPTION_HISTORICAL_VOLATILITY -> optionHistoricalVolatility = value;
                case OPTION_IMPLIED_VOLATILITY -> optionImpliedVolatility = value;
                case INDEX_FUTURE_PREMIUM -> indexFuturePremium = value;
                case SHORTABLE -> shortable = value;
                case HALTED -> halted = value;
                case TRADE_COUNT -> tradeCount = value;
                case TRADE_RATE -> tradeRate = value;
                case VOLUME_RATE -> volumeRate = value;
                case RT_HISTORICAL_VOLATILITY -> rtHistoricalVolatility = value;
                default ->
                    log.warn("Unknown generic type for tick, type={}, value={}", type, value);
            }
            log.trace("Set value for {}: {} = {}", tickerId, tickType, value);
        });
    }

    public void refreshUpdateTime() {
        updateTime = LocalDateTime.now();
    }

    public enum Types {
        UNKNOWN(-1),
        BID_SIZE(0),
        BID(1),
        ASK(2),
        ASK_SIZE(3),
        LAST(4),
        LAST_SIZE(5),
        HIGH(6),
        LOW(7),
        VOLUME(8),
        CLOSE_PRICE(9),
        OPEN_TICK(14),
        LOW13_WEEKS(15),
        HIGH13_WEEKS(16),
        LOW26_WEEKS(17),
        HIGH26_WEEKS(18),
        LOW52_WEEKS(19),
        HIGH52_WEEKS(20),
        VOLUME_AVERAGE(21),
        OPTION_HISTORICAL_VOLATILITY(23),
        OPTION_IMPLIED_VOLATILITY(24),
        OPTION_CALL_OPEN_INTEREST(27),
        OPTION_PUT_OPEN_INTEREST(28),
        OPTION_CALL_VOLUME(29),
        OPTION_PUT_VOLUME(30),
        INDEX_FUTURE_PREMIUM(31),
        BID_EXCHANGE(32),
        ASK_EXCHANGE(33),
        ACTION_VOLUME(34),
        AUCTION_PRICE(35),
        ACTION_IMBALANCE(36),
        MARK_PRICE(37),
        LAST_TIMESTAMP(45),
        SHORTABLE(46),
        RT_VOLUME(48),
        HALTED(49),
        BID_YIELD(50),
        ASK_YIELD(51),
        LAST_YIELD(52),
        TRADE_COUNT(54),
        TRADE_RATE(55),
        VOLUME_RATE(56),
        LAST_RTHTRADE(57),
        RT_HISTORICAL_VOLATILITY(58),
        IB_DIVIDENDS(59),
        REGULATORY_IMBALANCE(61),
        NEWS(62),
        SHORT_TERM_VOLUME3_MIN(63),
        SHORT_TERM_VOLUME5_MIN(64),
        SHORT_TERM_VOLUME10_MIN(65),
        DELAYED_BID(66),
        DELAYED_ASK(67),
        DELAYED_LAST(68),
        DELAYED_BID_SIZE(69),
        DELAYED_ASK_SIZE(70),
        DELAYED_LAST_SIZE(71),
        DELAYED_HIGH_PRICE(72),
        DELAYED_LOW_PRICE(73),
        DELAYED_VOLUME(74),
        DELAYED_CLOSE(75),
        DELAYED_OPEN(76),
        RT_TRADE_VOLUME(77),
        CREDITMAN_MARK_PRICE(78),
        CREDITMAN_SLOW_MAR_KPRICE(79),
        DELAYED_BID_OPTION(80),
        DELAYED_ASK_OPTION(81),
        DELAYED_LAST_OPTION(82),
        DELAYED_MODEL_OPTION(83),
        LAST_EXCHANGE(84),
        LAST_REGULATORY_TIME(85),
        FUTURES_OPEN_INTEREST(86),
        AVERAGE_OPTION_VOLUME(87),
        DELAYED_LAST_TIMESTAMP(88),
        SHORTABLE_SHARES(89);

        private static final Map<Integer, Types> map = new HashMap<>();

        static {
            for (Types type : values()) {
                map.put(type.value, type);
            }
        }

        private final Integer value;

        Types(Integer value) {
            this.value = value;
        }

        static Optional<Types> valueOf(Integer type) {
            Types value = map.get(type);
            if (value == null) {
                log.warn("Unknown tick type: {}", type);
            }
            return Optional.ofNullable(value);
        }

        public Integer getValue() {
            return value;
        }
    }
}
