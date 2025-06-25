package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.entity.PostDamageDealtEvent;

import java.util.Objects;

public final class ChannelingPostDamageDealtEvent extends PostDamageDealtEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingPostDamageDealtEvent(
            LivingEntity entity,
            LivingEntity attacker,
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
