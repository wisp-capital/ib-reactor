package reactor.types;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class IbOrder {
    private final int orderId;
    private final Contract contract;
    private final Order order;
    private final OrderState state;

    private final LinkedList<IbOrderStatus> statuses = new LinkedList<>();

    public synchronized boolean addStatus(IbOrderStatus status) {
        if (!statuses.contains(status)) {
            return statuses.add(status);
        }

        return false;
    }

    public synchronized void addStatuses(List<IbOrderStatus> list) {
        statuses.addAll(list);
    }

    public synchronized IbOrderStatus getLastStatus() {
        return statuses.getLast();
    }
}
