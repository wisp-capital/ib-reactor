package reactor.types;

import lombok.Data;

@Data
public class IbExecutionReport {

    private IbContract contract;
    private IbExecution execution;
    private IbCommissionReport commission;

    public IbExecutionReport(IbContract contract, IbExecution execution) {
        this.contract = contract;
        this.execution = execution;
    }

    public void setCommission(IbCommissionReport commission) {
        this.commission = commission;
    }
}
