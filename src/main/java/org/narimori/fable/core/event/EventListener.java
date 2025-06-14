package org.narimori.fable.core.event;

import org.jetbrains.annotations.NotNull;

public interface EventListener<E extends Event> {
    void handle(@NotNull E event);
}
