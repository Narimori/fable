package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;

import java.util.Objects;

public final class ChannelingPostDamageTakenEvent extends ChannelingEvent {
    private final @NotNull DamageSource source;
    private final float baseDamageTaken;
    private final float damageTaken;
    private final boolean blocked;

    @ApiStatus.Internal
    public ChannelingPostDamageTakenEvent(
            @NotNull ChannelingContext context,
            @NotNull DamageSource source,
            float baseDamageTaken,
            float damageTaken,
            boolean blocked
    ) {
        super(context);
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.baseDamageTaken = baseDamageTaken;
        this.damageTaken = damageTaken;
        this.blocked = blocked;
    }

    public @NotNull DamageSource getSource() {
        return source;
    }

    public float getBaseDamageTaken() {
        return baseDamageTaken;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
