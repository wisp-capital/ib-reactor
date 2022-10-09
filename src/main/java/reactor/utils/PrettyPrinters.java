package reactor.utils;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Order;
import com.ib.client.PriceIncrement;

import java.util.stream.Stream;

@SuppressWarnings({"WeakerAccess"})
public class PrettyPrinters {

    public static String contractToString(Contract contract) {
        return "{" + "conid=" + contract.conid() + ", symbol=" + contract.symbol() + ", currency=" +
            contract.currency() + ", exchange=" + contract.exchange() + ", multiplier=" +
            contract.multiplier() + ", lastTradeDateOrContractMonth=" +
            contract.lastTradeDateOrContractMonth() + ", localSymbol=" + contract.localSymbol() +
            "}";
    }

    public static String orderToString(Order order) {

        final StringBuilder buffer = new StringBuilder("{");
        buffer.append("id=").append(order.orderId());
        buffer.append(", action=").append(order.action());

        if (order.orderType() != null) {
            buffer.append(", orderType=").append(order.orderType());
        }
        if (order.auxPrice() != Double.MAX_VALUE) {
            buffer.append(", auxPrice=").append(order.auxPrice());
        }
        if (order.lmtPrice() != Double.MAX_VALUE) {
            buffer.append(", lmtPrice=").append(order.lmtPrice());
        }
        if (order.tif() != null) {
            buffer.append(", tif=").append(order.tif());
        }

        buffer.append(", totalQuantity=").append(order.totalQuantity());
        buffer.append(", outsideRth=").append(order.outsideRth());
        buffer.append("}");
        return buffer.toString();
    }

    public static String contractDetailsToString(ContractDetails details) {
        return "{" + "contract=[" + contractToString(details.contract()) + "]" + ", conid=" +
            details.conid() + ", minTick=" + details.minTick() + ", validExchanges=" +
            details.validExchanges();
    }

    public static String priceIncrementsToString(PriceIncrement[] increments) {

        final StringBuffer buffer = new StringBuffer("{");

        Stream.of(increments).forEach(inc -> {
            buffer.append("lowEdge=").append(inc.lowEdge());
            buffer.append(", increment=").append(inc.increment());
            buffer.append(";");
        });
        buffer.append("}");
        return buffer.toString();
    }

    public static String priceIncrementToString(PriceIncrement increment) {
        return "{" + "lowEdge=" + increment.lowEdge() + ", increment=" + increment.increment() +
            '}';
    }
}
