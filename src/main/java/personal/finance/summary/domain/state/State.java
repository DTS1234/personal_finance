package personal.finance.summary.domain.state;

import personal.finance.summary.domain.action.Action;

public interface State {

    void executeAction(Action action);

}
