package org.narimori.fable.channeling.event.lifecycle;

import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;

import java.util.Objects;

public final class ChannelingStartEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingStartEvent(ChannelingContext context) {
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public ChannelingContext getContext() {
        return context;
    }
}
