package personal.finance.common.events;

public interface EventListener<T> {

    void handle(T event);

}
