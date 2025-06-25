package org.narimori.fable.core.event.dispatch;

import org.narimori.fable.core.event.Cancellable;
import org.narimori.fable.core.event.Event;
import org.narimori.fable.core.event.EventListener;

import java.util.*;
import java.util.stream.Collectors;

public final class EventDispatcher<E extends Event> {
    private final Map<Class<? extends E>, List<EventListener<? extends E>>> eventListeners;

    private EventDispatcher(Map<Class<? extends E>, List<EventListener<? extends E>>> eventListeners) {
        this.eventListeners = Objects.requireNonNull(eventListeners)
                .entrySet()
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                Map.Entry::getKey,
                                entry -> List.copyOf(entry.getValue())
                        )
                );
    }

    public static <E extends Event> EventDispatcher<E> empty() {
        return new EventDispatcher<>(Map.of());
    }

    public <S extends E> EventDispatcher<E> addListener(Class<S> eventClass, EventListener<S> eventListener) {
        Objects.requireNonNull(eventClass);
        Objects.requireNonNull(eventListener);

        List<EventListener<? extends E>> oldList = eventListeners.get(eventClass);
        List<EventListener<? extends E>> newList;
        if (oldList == null || oldList.isEmpty()) {
            newList = List.of(eventListener);
        } else {
            newList = new ArrayList<>(oldList);
            newList.add(eventListener);
        }

        Map<Class<? extends E>, List<EventListener<? extends E>>> newMap = new HashMap<>(eventListeners);
        newMap.put(eventClass, Collections.unmodifiableList(newList));
        return new EventDispatcher<>(Collections.unmodifiableMap(newMap));
    }

    public <S extends E> EventDispatcher<E> removeListener(Class<S> eventClass, EventListener<S> eventListener) {
        Objects.requireNonNull(eventClass);
        Objects.requireNonNull(eventListener);

        List<EventListener<? extends E>> oldList = eventListeners.get(eventClass);
        if (oldList == null || oldList.isEmpty()) {
            return this;
        }

        List<EventListener<? extends E>> newList = new ArrayList<>(oldList);
        if (!newList.remove(eventListener)) {
            return this;
        }

        Map<Class<? extends E>, List<EventListener<? extends E>>> newMap = new HashMap<>(eventListeners);
        if (newList.isEmpty()) {
            newMap.remove(eventClass);
        } else {
            newMap.put(eventClass, Collections.unmodifiableList(newList));
        }

        return new EventDispatcher<>(Collections.unmodifiableMap(newMap));
    }

    @SuppressWarnings("unchecked")
    public boolean dispatch(E event) {
        Objects.requireNonNull(event);

        List<EventListener<? extends E>> listeners = eventListeners.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return true;
        }

        Cancellable cancellable = event instanceof Cancellable ? (Cancellable) event : null;
        for (EventListener<? extends E> listener : listeners) {
            ((EventListener<E>) listener).handle(event);
            if (cancellable != null && cancellable.isCancelled()) {
                return false;
            }
        }

        return true;
    }
}
