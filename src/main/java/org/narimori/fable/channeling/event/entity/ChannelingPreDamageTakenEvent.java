package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.Cancellable;

import java.util.Objects;

public final class ChannelingPreDamageTakenEvent extends ChannelingEvent implements Cancellable {
    private final @NotNull DamageSource source;
    private final float amount;

    private boolean cancelled = false;

    @ApiStatus.Internal
    public ChannelingPreDamageTakenEvent(
            @NotNull ChannelingContext context,
            @NotNull DamageSource source,
            float amount
    ) {
        super(context);
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.amount = amount;
    }

    public @NotNull DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
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
