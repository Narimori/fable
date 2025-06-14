package org.narimori.fable.channeling.event.lifecycle;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.Cancellable;

public final class ChannelingInterruptEvent extends ChannelingEvent implements Cancellable {
    private boolean cancelled = false;

    @ApiStatus.Internal
    public ChannelingInterruptEvent(@NotNull ChannelingContext context) {
        super(context);
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
