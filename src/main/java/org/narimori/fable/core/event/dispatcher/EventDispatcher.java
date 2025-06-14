package org.narimori.fable.core.event.dispatcher;

import org.jetbrains.annotations.NotNull;
import org.narimori.fable.core.event.Event;
import org.narimori.fable.core.event.EventListener;

import java.util.*;

public final class EventDispatcher<E extends Event> {
    private final @NotNull Map<Class<? extends E>, List<EventListener<? extends E>>> listenersMap;

    private EventDispatcher(
            @NotNull Map<Class<? extends E>, List<EventListener<? extends E>>> listenersMap
    ) {
        this.listenersMap = Map.copyOf(
                Objects.requireNonNull(listenersMap, "listenersMap must not be null")
        );
    }

    public static <E extends Event> @NotNull EventDispatcher<E> create() {
        return new EventDispatcher<>(Map.of());
    }

    public <T extends E> @NotNull EventDispatcher<E> addListener(
            @NotNull Class<T> eventClass,
            @NotNull EventListener<T> eventListener
    ) {
        Objects.requireNonNull(eventClass, "eventClass must not be null");
        Objects.requireNonNull(eventListener, "eventListener must not be null");

        List<EventListener<? extends E>> oldList = listenersMap.getOrDefault(eventClass, Collections.emptyList());
        List<EventListener<? extends E>> newList = new ArrayList<>(oldList.size() + 1);
        newList.addAll(oldList);
        newList.add(eventListener);

        Map<Class<? extends E>, List<EventListener<? extends E>>> newMap = new HashMap<>(listenersMap);
        newMap.put(eventClass, Collections.unmodifiableList(newList));

        return new EventDispatcher<>(newMap);
    }

    public <T extends E> @NotNull EventDispatcher<E> removeListener(
            @NotNull Class<T> eventClass,
            @NotNull EventListener<T> eventListener
    ) {
        Objects.requireNonNull(eventClass, "eventClass must not be null");
        Objects.requireNonNull(eventListener, "eventListener must not be null");

        List<EventListener<? extends E>> oldList = listenersMap.get(eventClass);
        if (oldList == null || oldList.isEmpty()) {
            return this;
        }

        List<EventListener<? extends E>> newList = new ArrayList<>(oldList);
        if (!newList.remove(eventListener)) {
            return this;
        }

        Map<Class<? extends E>, List<EventListener<? extends E>>> newMap = new HashMap<>(listenersMap);
        if (newList.isEmpty()) {
            newMap.remove(eventClass);
        } else {
            newMap.put(eventClass, Collections.unmodifiableList(newList));
        }

        return new EventDispatcher<>(newMap);
    }

    @SuppressWarnings("unchecked")
    public void dispatch(@NotNull E event) {
        Objects.requireNonNull(event, "event must not be null");

        List<EventListener<? extends E>> listeners = listenersMap.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        for (EventListener<? extends E> listener : listeners) {
            ((EventListener<E>) listener).handle(event);
        }
    }
}
