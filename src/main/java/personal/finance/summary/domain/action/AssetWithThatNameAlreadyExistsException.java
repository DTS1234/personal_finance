package personal.finance.summary.domain.action;

import lombok.AllArgsConstructor;


public class AssetWithThatNameAlreadyExistsException extends RuntimeException {

    public AssetWithThatNameAlreadyExistsException(String name) {
        super(String.format("Asset with %s name already exists!", name));
    }
}
