package org.narimori.fable.core.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.core.event.Event;

import java.util.Objects;

public abstract class PostDamageTakenEvent implements Event {
    private final LivingEntity entity;
    private final @Nullable Entity attacker;
    private final DamageSource source;
    private final float baseDamageTaken;
    private final float damageTaken;
    private final boolean blocked;

    protected PostDamageTakenEvent(
            LivingEntity entity,
            @Nullable Entity attacker,
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

    public @Nullable Entity getAttacker() {
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
