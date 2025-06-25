package org.narimori.fable.channeling.event.lifecycle;

import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.Cancellable;

import java.util.Objects;

public final class ChannelingCancelEvent implements ChannelingEvent, Cancellable {
    private final ChannelingContext context;
    private boolean cancelled;

    public ChannelingCancelEvent(ChannelingContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public ChannelingContext getContext() {
        return context;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
