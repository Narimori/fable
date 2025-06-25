package org.narimori.fable.skill.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.core.event.entity.PreDamageTakenEvent;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class SkillPreDamageTakenEvent extends PreDamageTakenEvent implements SkillEvent {
    private final SkillContext context;

    public SkillPreDamageTakenEvent(
            LivingEntity entity,
            @Nullable Entity attacker,
            DamageSource source,
            float amount,
            SkillContext context
    ) {
        super(entity, attacker, source, amount);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public SkillContext getContext() {
        return context;
    }
}
