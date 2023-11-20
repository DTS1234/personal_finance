package personal.finance.summary.application.exceptions;

public class NoSummaryInDraftException extends RuntimeException {
    public NoSummaryInDraftException(String message) {
        super(message);
    }
}
