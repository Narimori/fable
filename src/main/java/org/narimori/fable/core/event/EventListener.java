package org.narimori.fable.core.event;

@FunctionalInterface
public interface EventListener<E extends Event> {
    void handle(E event);
}
