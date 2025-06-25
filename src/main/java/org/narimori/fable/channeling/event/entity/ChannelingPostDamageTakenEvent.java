package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.entity.PostDamageTakenEvent;

import java.util.Objects;

public final class ChannelingPostDamageTakenEvent extends PostDamageTakenEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingPostDamageTakenEvent(
            LivingEntity entity,
            @Nullable Entity attacker,
            DamageSource source,
            float baseDamageTaken,
            float damageTaken,
            boolean blocked,
            ChannelingContext context
    ) {
        super(entity, attacker, source, baseDamageTaken, damageTaken, blocked);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public ChannelingContext getContext() {
        return context;
    }
}
