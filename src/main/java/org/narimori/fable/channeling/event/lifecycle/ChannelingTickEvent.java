package org.narimori.fable.channeling.event.lifecycle;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;

public final class ChannelingTickEvent extends ChannelingEvent {
    @ApiStatus.Internal
    public ChannelingTickEvent(@NotNull ChannelingContext context) {
        super(context);
    }
}
