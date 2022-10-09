package reactor.types;

import com.ib.client.ContractDescription;
import com.ib.client.Types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IbContractDescription {

    private final IbContract contract;
    private final List<Types.SecType> derivativeSecTypes;

    public IbContractDescription(ContractDescription base) {
        contract = new IbContract(base.contract());

        derivativeSecTypes = Stream.of(base.derivativeSecTypes())
            .map(Types.SecType::get)
            .collect(Collectors.toList());
    }

    public IbContract getContract() {
        return contract;
    }

    public List<Types.SecType> getDerivativeSecTypes() {
        return derivativeSecTypes;
    }

    @Override
    public String toString() {
        return "{" + "contract=" + contract + ", derivativeSecTypes=" + derivativeSecTypes + '}';
    }
}
