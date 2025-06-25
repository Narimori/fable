package org.narimori.fable.skill.event.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.narimori.fable.core.event.entity.PreDamageDealtEvent;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class SkillPreDamageDealtEvent extends PreDamageDealtEvent implements SkillEvent {
    private final SkillContext context;

    public SkillPreDamageDealtEvent(
            LivingEntity entity,
            LivingEntity attacker,
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
