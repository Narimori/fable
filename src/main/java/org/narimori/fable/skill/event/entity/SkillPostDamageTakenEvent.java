package org.narimori.fable.skill.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.core.event.entity.PostDamageTakenEvent;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class SkillPostDamageTakenEvent extends PostDamageTakenEvent implements SkillEvent {
    private final SkillContext context;

    public SkillPostDamageTakenEvent(
            LivingEntity entity,
            @Nullable Entity attacker,
            DamageSource source,
            float baseDamageTaken,
            float damageTaken,
            boolean blocked,
            SkillContext context
    ) {
        super(entity, attacker, source, baseDamageTaken, damageTaken, blocked);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public SkillContext getContext() {
        return context;
    }
}
