package org.narimori.fable.channeling.event;

import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.core.event.Event;

import java.util.Objects;

public abstract class ChannelingEvent implements Event {
    private final @NotNull ChannelingContext context;

    protected ChannelingEvent(@NotNull ChannelingContext context) {
        this.context = Objects.requireNonNull(context, "context must not be null");
    }

    public @NotNull ChannelingContext getContext() {
        return context;
    }
}
