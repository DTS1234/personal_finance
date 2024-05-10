package personal.finance.common.events;

import java.util.ArrayList;
import java.util.List;

public class FakeEventPublisher implements EventPublisher {

    public final List<Event> publishedStore = new ArrayList<>();

    @Override
    public void publishEvent(Event event) {
        this.publishedStore.add(event);
    }
}
