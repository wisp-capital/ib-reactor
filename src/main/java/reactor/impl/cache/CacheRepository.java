package reactor.impl.cache;

import com.google.common.collect.ImmutableMap;
import com.ib.client.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.types.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CacheRepository {

    private static final Logger log = LoggerFactory.getLogger(CacheRepository.class);

    private final ConcurrentHashMap<Integer, IbOrder> orders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<PositionKey, IbPosition> positions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, IbTick> ticks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, IbPortfolio> portfolioContracts =
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, IbAccountsSummary> accountSummaries =
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, IbExecutionReport> execReports =
        new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, Map<IbMarketDepth.Key, IbMarketDepth>> orderBooks =
        new ConcurrentHashMap<>();

    public boolean addOrder(IbOrder order) {

        final AtomicReference<Boolean> result = new AtomicReference<>(false);
        orders.compute(order.getOrderId(), (key, value) -> {
            if (value != null) {
                log.debug("Order {} already has been added", order.getOrderId());
                result.set(false);
                order.addStatuses(value.getStatuses());
                return order;
            }

            result.set(true);
            return order;
        });

        return result.get();
    }

    public Map<Integer, IbOrder> getOrders() {
        return ImmutableMap.copyOf(orders);
    }

    public boolean addNewStatus(IbOrderStatus status) {
        IbOrder order = orders.get(status.getOrderId());
        if (order == null) {
            log.error("Status update for not (yet?) existing order {}: {}",
                status.getOrderId(),
                status
            );
            return false;
        }

        return order.addStatus(status);
    }

    public void updatePosition(IbPosition position) {
        positions.put(new PositionKey(position.getAccount(), position.getContract().conid()),
            position
        );
    }

    public void updatePortfolio(IbPortfolio portfolio) {
        portfolioContracts.put(portfolio.getContract().conid(), portfolio);
    }

    public IbTick updateTick(int tickerId, Consumer<IbTick> consumer) {
        IbTick tick = ticks.computeIfAbsent(tickerId, (key) -> new IbTick());
        tick.refreshUpdateTime();
        consumer.accept(tick);
        return tick;
    }

    public void updateAccountsSummary(
        int id, String account, String tag, String value, String currency
    ) {
        IbAccountsSummary accountsSummary =
            accountSummaries.computeIfAbsent(id, (key) -> new IbAccountsSummary());
        try {
            accountsSummary.update(account, tag, value, currency);
        } catch (Exception e) {
            log.error("Cannot update account summary: {}", e.getMessage(), e);
        }
    }

    public IbAccountsSummary popAccountsSummary(int id) {
        return accountSummaries.remove(id);
    }

    public Map<IbMarketDepth.Key, IbMarketDepth> getOrderBook(Contract contract) {
        Objects.requireNonNull(contract, "'contract' parameter is null");
        if (contract.conid() == 0) {
            throw new IllegalArgumentException("contract ID is missing");
        }

        return orderBooks.get(contract.conid());
    }

    public IbTick getTick(int tickerId) {
        return ticks.get(tickerId);
    }

    public Collection<IbPosition> getPositions() {
        return Collections.unmodifiableCollection(positions.values());
    }

    public Collection<IbPortfolio> getPortfolio() {
        return Collections.unmodifiableCollection(portfolioContracts.values());
    }

    public IbPosition getPosition(String account, Contract contract) {
        Objects.requireNonNull(account);
        Objects.requireNonNull(contract);
        if (contract.conid() == 0) {
            throw new IllegalArgumentException("Contract has a id 0");
        }
        return positions.get(new PositionKey(account, contract.conid()));
    }

    public IbPortfolio getPortfolio(Contract contract) {
        Objects.requireNonNull(contract);
        if (contract.conid() == 0) {
            throw new IllegalArgumentException("Contract has a id 0");
        }
        return portfolioContracts.get(contract.conid());
    }

    public void addMarketDepth(
        Contract contract, IbMarketDepth marketDepth, IbMarketDepth.Operation operation
    ) {

        orderBooks.compute(contract.conid(), (key, value) -> {
            Map<IbMarketDepth.Key, IbMarketDepth> keyIbMarketDepthMap = value;
            if (keyIbMarketDepthMap == null) {
                keyIbMarketDepthMap = new HashMap<>();
            }

            switch (operation) {
                case INSERT -> {
                    log.trace("Market depth is added: {}", marketDepth);
                    keyIbMarketDepthMap.put(marketDepth.key(), marketDepth);
                }
                case UPDATE -> {
                    log.trace("Market depth is updated: {}", marketDepth);
                    keyIbMarketDepthMap.put(marketDepth.key(), marketDepth);
                }
                case REMOVE -> {
                    log.trace("Market depth is removed: {}", marketDepth);
                    keyIbMarketDepthMap.remove(marketDepth.key());
                }
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            }

            return keyIbMarketDepthMap;
        });
    }

    public void addExecutionReport(IbContract contract, IbExecution execution) {
        IbExecutionReport report =
            execReports.put(execution.getExecId(), new IbExecutionReport(contract, execution));
        if (report != null) {
            log.warn("Execution info for '{}' is overwritten", execution.getExecId());
        }
    }

    public Optional<IbExecutionReport> updateExecutionReport(IbCommissionReport report) {
        IbExecutionReport execReport = execReports.get(report.getExecId());
        if (execReport != null) {
            execReport.setCommission(report);
        } else {
            log.warn("Commission report for '{}' without execution report", report.getExecId());
        }

        return Optional.ofNullable(execReport);
    }

    public void clear() {
        orders.clear();
        positions.clear();
        ticks.clear();
        portfolioContracts.clear();
        orderBooks.clear();
        execReports.clear();

        log.debug("Cache is cleared");
    }
}
