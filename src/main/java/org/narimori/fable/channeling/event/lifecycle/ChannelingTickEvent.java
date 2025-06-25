package org.narimori.fable.channeling.event.lifecycle;

import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;

import java.util.Objects;

public final class ChannelingTickEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingTickEvent(ChannelingContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public ChannelingContext getContext() {
        return context;
    }
}
