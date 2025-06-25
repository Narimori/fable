package org.narimori.fable.core.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.narimori.fable.core.event.Cancellable;
import org.narimori.fable.core.event.Event;

import java.util.Objects;

public abstract class PreDamageDealtEvent implements Event, Cancellable {
    private final LivingEntity entity;
    private final LivingEntity attacker;
    private final DamageSource source;
    private final float amount;
    private boolean cancelled;

    protected PreDamageDealtEvent(
            LivingEntity entity,
            LivingEntity attacker,
            DamageSource source,
            float amount
    ) {
        this.entity = Objects.requireNonNull(entity);
        this.attacker = Objects.requireNonNull(attacker);
        this.source = Objects.requireNonNull(source);
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public LivingEntity getAttacker() {
        return attacker;
    }

    public DamageSource getSource() {
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
