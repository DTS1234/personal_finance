package personal.finance.summary.domain.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal.finance.summary.domain.action.Action;

import java.util.List;

@AllArgsConstructor
public class ConfirmedState implements State {

    @Getter
    private List<Action> actions;

    @Override
    public void executeAction(Action action) {

    }
}

