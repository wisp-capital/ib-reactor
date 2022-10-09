package reactor.types;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Data
public class IbAccountSummary {
    private final Map<String, DetailsPerCurrency> details = new HashMap<>();
    private String accountType;
    private BigDecimal netLiquidation;
    private BigDecimal totalCashValue;
    private BigDecimal settledCash;
    private BigDecimal buyingPower;
    private BigDecimal equityWithLoanValue;
    private BigDecimal previousEquityWithLoanValue;
    private BigDecimal grossPositionValue;
    private BigDecimal regTEquity;
    private BigDecimal regTMargin;
    private BigDecimal sma;
    private BigDecimal initMarginReq;
    private BigDecimal maintMarginReq;
    private BigDecimal availableFunds;
    private BigDecimal excessLiquidity;
    private BigDecimal cushion;
    private BigDecimal fullInitMarginReq;
    private BigDecimal fullMaintMarginReq;
    private BigDecimal fullAvailableFunds;
    private BigDecimal fullExcessLiquidity;
    private Date lookAheadNextChange;
    private BigDecimal lookAheadInitMarginReq;
    private BigDecimal lookAheadMaintMarginReq;
    private BigDecimal lookAheadAvailableFunds;
    private BigDecimal lookAheadExcessLiquidity;
    private Integer highestSeverity;
    private Integer dayTradesRemaining;
    private Integer leverage;

    @SuppressWarnings("checkstyle:MethodLength")
    public void update(String tag, String value, String currency) throws Exception {

        switch (tag) {
            case "AccountType" -> shouldBeNull(accountType, () -> accountType = value);
            case "NetLiquidation" -> shouldBeNull(netLiquidation, () -> netLiquidation = new BigDecimal(value));
            case "TotalCashValue" -> shouldBeNull(totalCashValue, () -> totalCashValue = new BigDecimal(value));
            case "SettledCash" -> shouldBeNull(settledCash, () -> settledCash = new BigDecimal(value));
            case "BuyingPower" -> shouldBeNull(buyingPower, () -> buyingPower = new BigDecimal(value));
            case "EquityWithLoanValue" -> shouldBeNull(equityWithLoanValue,
                () -> equityWithLoanValue = new BigDecimal(value)
            );
            case "PreviousEquityWithLoanValue" -> shouldBeNull(previousEquityWithLoanValue,
                () -> previousEquityWithLoanValue = new BigDecimal(value)
            );
            case "GrossPositionValue" -> shouldBeNull(grossPositionValue, () -> grossPositionValue = new BigDecimal(value));
            case "RegTEquity" -> shouldBeNull(regTEquity, () -> regTEquity = new BigDecimal(value));
            case "RegTMargin" -> shouldBeNull(regTMargin, () -> regTMargin = new BigDecimal(value));
            case "SMA" -> shouldBeNull(sma, () -> sma = new BigDecimal(value));
            case "InitMarginReq" -> shouldBeNull(initMarginReq, () -> initMarginReq = new BigDecimal(value));
            case "MaintMarginReq" -> shouldBeNull(maintMarginReq, () -> maintMarginReq = new BigDecimal(value));
            case "AvailableFunds" -> shouldBeNull(availableFunds, () -> availableFunds = new BigDecimal(value));
            case "ExcessLiquidity" -> shouldBeNull(excessLiquidity, () -> excessLiquidity = new BigDecimal(value));
            case "Cushion" -> shouldBeNull(cushion, () -> cushion = new BigDecimal(value));
            case "FullInitMarginReq" -> shouldBeNull(fullInitMarginReq, () -> fullInitMarginReq = new BigDecimal(value));
            case "FullMaintMarginReq" -> shouldBeNull(fullMaintMarginReq, () -> fullMaintMarginReq = new BigDecimal(value));
            case "FullAvailableFunds" -> shouldBeNull(fullAvailableFunds, () -> fullAvailableFunds = new BigDecimal(value));
            case "FullExcessLiquidity" -> shouldBeNull(fullExcessLiquidity,
                () -> fullExcessLiquidity = new BigDecimal(value)
            );
            case "LookAheadNextChange" -> {
                long ms = TimeUnit.SECONDS.toMillis(Integer.parseInt(value));
                shouldBeNull(lookAheadNextChange, () -> lookAheadNextChange = new Date(ms));
            }
            case "LookAheadInitMarginReq" -> shouldBeNull(lookAheadInitMarginReq,
                () -> lookAheadInitMarginReq = new BigDecimal(value)
            );
            case "LookAheadMaintMarginReq" -> shouldBeNull(lookAheadMaintMarginReq,
                () -> lookAheadMaintMarginReq = new BigDecimal(value)
            );
            case "LookAheadAvailableFunds" -> shouldBeNull(lookAheadAvailableFunds,
                () -> lookAheadAvailableFunds = new BigDecimal(value)
            );
            case "LookAheadExcessLiquidity" -> shouldBeNull(lookAheadExcessLiquidity,
                () -> lookAheadExcessLiquidity = new BigDecimal(value)
            );
            case "HighestSeverity" -> shouldBeNull(highestSeverity, () -> highestSeverity = Integer.valueOf(value));
            case "DayTradesRemaining" -> shouldBeNull(dayTradesRemaining, () -> dayTradesRemaining = Integer.valueOf(value));
            case "Leverage" -> shouldBeNull(leverage, () -> leverage = Integer.valueOf(value));


            // details per currency

            case "Currency" -> applyDetails(currency, summary -> summary.currency = value);
            case "RealCurrency" -> applyDetails(currency, summary -> summary.realCurrency = value);
            case "CashBalance" -> applyDetails(currency, summary -> summary.cashBalance = new BigDecimal(value));
            case "TotalCashBalance" -> applyDetails(currency, summary -> summary.totalCashBalance = new BigDecimal(value));
            case "AccruedCash" -> applyDetails(currency, summary -> summary.accruedCash = new BigDecimal(value));
            case "StockMarketValue" -> applyDetails(currency, summary -> summary.stockMarketValue = new BigDecimal(value));
            case "OptionMarketValue" -> applyDetails(currency,
                summary -> summary.optionMarketValue = new BigDecimal(value)
            );
            case "FutureOptionValue" -> applyDetails(currency,
                summary -> summary.futureOptionValue = new BigDecimal(value)
            );
            case "FuturesPNL" -> applyDetails(currency, summary -> summary.futuresPNL = new BigDecimal(value));
            case "NetLiquidationByCurrency" -> applyDetails(currency,
                summary -> summary.netLiquidationByCurrency = new BigDecimal(value)
            );
            case "UnrealizedPnL" -> applyDetails(currency, summary -> summary.unrealizedPnL = new BigDecimal(value));
            case "RealizedPnL" -> applyDetails(currency, summary -> summary.realizedPnL = new BigDecimal(value));
            case "ExchangeRate" -> applyDetails(currency, summary -> summary.exchangeRate = new BigDecimal(value));
            case "FundValue" -> applyDetails(currency, summary -> summary.fundValue = new BigDecimal(value));
            case "NetDividend" -> applyDetails(currency, summary -> summary.netDividend = new BigDecimal(value));
            case "MutualFundValue" -> applyDetails(currency, summary -> summary.mutualFundValue = new BigDecimal(value));
            case "MoneyMarketFundValue" -> applyDetails(currency,
                summary -> summary.moneyMarketFundValue = new BigDecimal(value)
            );
            case "CorporateBondValue" -> applyDetails(currency,
                summary -> summary.corporateBondValue = new BigDecimal(value)
            );
            case "TBondValue" -> applyDetails(currency, summary -> summary.tBondValue = new BigDecimal(value));
            case "TBillValue" -> applyDetails(currency, summary -> summary.tBillValue = new BigDecimal(value));
            case "WarrantValue" -> applyDetails(currency, summary -> summary.warrantValue = new BigDecimal(value));
            case "FxCashBalance" -> applyDetails(currency, summary -> summary.fxCashBalance = new BigDecimal(value));
            case "AccountOrGroup" -> applyDetails(currency, summary -> summary.accountOrGroup = value);
            case "IssuerOptionValue" -> applyDetails(currency,
                summary -> summary.issuerOptionValue = new BigDecimal(value)
            );
            default -> throw new IllegalStateException("Unexpected value: " + tag);
        }
    }

    private <T> void shouldBeNull(T member, Runnable runnable) throws Exception {

        if (member != null) {
            throw new Exception("Value is overwritten");
        }
        runnable.run();
    }

    private void applyDetails(String currency, Consumer<DetailsPerCurrency> applyValue) {
        DetailsPerCurrency summary = details.computeIfAbsent(currency, key -> new DetailsPerCurrency());
        applyValue.accept(summary);
    }

    @Data
    public class DetailsPerCurrency {
        private String currency;
        private String realCurrency;
        private BigDecimal cashBalance;
        private BigDecimal totalCashBalance;
        private BigDecimal accruedCash;
        private BigDecimal stockMarketValue;
        private BigDecimal optionMarketValue;
        private BigDecimal futureOptionValue;
        private BigDecimal futuresPNL;
        private BigDecimal netLiquidationByCurrency;
        private BigDecimal unrealizedPnL;
        private BigDecimal realizedPnL;
        private BigDecimal exchangeRate;
        private BigDecimal fundValue;
        private BigDecimal netDividend;
        private BigDecimal mutualFundValue;
        private BigDecimal moneyMarketFundValue;
        private BigDecimal corporateBondValue;
        private BigDecimal tBondValue;
        private BigDecimal tBillValue;
        private BigDecimal warrantValue;
        private BigDecimal fxCashBalance;
        private String accountOrGroup;
        private BigDecimal issuerOptionValue;
    }
}
