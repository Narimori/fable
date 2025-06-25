package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.entity.PreDamageDealtEvent;

import java.util.Objects;

public final class ChannelingPreDamageDealtEvent extends PreDamageDealtEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingPreDamageDealtEvent(
            LivingEntity entity,
            LivingEntity attacker,
            DamageSource source,
            float amount,
            ChannelingContext context
    ) {
        super(entity, attacker, source, amount);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public ChannelingContext getContext() {
        return context;
    }
}
