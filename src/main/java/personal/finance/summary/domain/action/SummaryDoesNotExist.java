package personal.finance.summary.domain.action;

public class SummaryDoesNotExist extends RuntimeException {
    public SummaryDoesNotExist(String message) {
        super(message);
    }
}
