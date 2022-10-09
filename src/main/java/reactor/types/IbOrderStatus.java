package reactor.types;

import com.ib.client.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IbOrderStatus {
    private final int orderId;
    private final OrderStatus status;
    private final BigDecimal filled;
    private final BigDecimal remaining;
    private final BigDecimal avgFillPrice;
    private final int permId;
    private final int parentId;
    private final BigDecimal lastFillPrice;
    private final int clientId;
    private final String whyHeld;
    private final BigDecimal mktCapPrice;

    public IbOrderStatus(
        final int orderId,
        final String status,
        final BigDecimal filled,
        final BigDecimal remaining,
        final BigDecimal avgFillPrice,
        final int permId,
        final int parentId,
        final BigDecimal lastFillPrice,
        final int clientId,
        final String whyHeld,
        final BigDecimal mktCapPrice
    ) {
        this.orderId = orderId;
        this.status = OrderStatus.get(status);
        this.filled = filled;
        this.remaining = remaining;
        this.avgFillPrice = avgFillPrice;
        this.permId = permId;
        this.parentId = parentId;
        this.lastFillPrice = lastFillPrice;
        this.clientId = clientId;
        this.whyHeld = whyHeld;
        this.mktCapPrice = mktCapPrice;
    }

    public boolean isCanceled() {
        return status == OrderStatus.ApiCancelled || status == OrderStatus.Cancelled;
    }

    public boolean isInactive() {
        return status == OrderStatus.Inactive;
    }

    public boolean isFilled() {
        return status == OrderStatus.Filled;
    }

    public boolean isActive() {
        return status.isActive();
    }
}
