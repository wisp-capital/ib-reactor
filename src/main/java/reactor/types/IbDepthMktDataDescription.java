package reactor.types;

import com.ib.client.DepthMktDataDescription;

@SuppressWarnings({"WeakerAccess"})
public class IbDepthMktDataDescription {
    private final DepthMktDataDescription parent;

    public IbDepthMktDataDescription(DepthMktDataDescription parent) {
        this.parent = parent;
    }

    public String getExchange() {
        return parent.exchange();
    }

    public void setExchange(String exchange) {
        parent.exchange(exchange);
    }

    public String getSecType() {
        return parent.secType();
    }

    public void setSecType(String secType) {
        parent.secType(secType);
    }

    public String getListingExch() {
        return parent.listingExch();
    }

    public void setListingExch(String listingExch) {
        parent.listingExch(listingExch);
    }

    public String getServiceDataType() {
        return parent.serviceDataType();
    }

    public void setServiceDataType(String serviceDataType) {
        parent.serviceDataType(serviceDataType);
    }

    public int getAggGroup() {
        return parent.aggGroup();
    }

    public void setAggGroup(int aggGroup) {
        parent.aggGroup(aggGroup);
    }

    @Override
    public String toString() {
        return "{" + "exchange='" + getExchange() + '\'' + ", secType='" + getSecType() + '\'' +
            ", listingExch='" + getListingExch() + '\'' + ", serviceDataType='" +
            getServiceDataType() + '\'' + ", aggGroup=" + getAggGroup() + '}';
    }
}
