package org.narimori.fable.channeling.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.entity.PreDamageTakenEvent;

import java.util.Objects;

public final class ChannelingPreDamageTakenEvent extends PreDamageTakenEvent implements ChannelingEvent {
    private final ChannelingContext context;

    public ChannelingPreDamageTakenEvent(
            LivingEntity entity,
            @Nullable Entity attacker,
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
