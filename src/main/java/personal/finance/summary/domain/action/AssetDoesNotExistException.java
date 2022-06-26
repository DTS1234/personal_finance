package personal.finance.summary.domain.action;

public class AssetDoesNotExistException extends RuntimeException {
    public AssetDoesNotExistException(String message) {
        super(message);
    }
}
