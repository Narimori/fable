package org.narimori.fable.skill.event.entity;

import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class SkillPostDamageTakenEvent extends SkillEvent {
    private final @NotNull DamageSource source;
    private final float baseDamageTaken;
    private final float damageTaken;
    private final boolean blocked;

    @ApiStatus.Internal
    public SkillPostDamageTakenEvent(
            @NotNull SkillContext context,
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
