package org.narimori.fable.core.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.narimori.fable.core.event.Event;

import java.util.Objects;

public abstract class PostDamageDealtEvent implements Event {
    private final LivingEntity entity;
    private final LivingEntity attacker;
    private final DamageSource source;
    private final float baseDamageTaken;
    private final float damageTaken;
    private final boolean blocked;

    protected PostDamageDealtEvent(
            LivingEntity entity,
            LivingEntity attacker,
            DamageSource source,
            float baseDamageTaken,
            float damageTaken,
            boolean blocked
    ) {
        this.entity = Objects.requireNonNull(entity);
        this.attacker = Objects.requireNonNull(attacker);
        this.source = Objects.requireNonNull(source);
        this.baseDamageTaken = baseDamageTaken;
        this.damageTaken = damageTaken;
        this.blocked = blocked;
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
