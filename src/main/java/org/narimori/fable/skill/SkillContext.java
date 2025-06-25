package org.narimori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public final class SkillContext {
    private final RegistryEntry<Skill> skill;
    private final LivingEntity source;

    @ApiStatus.Internal
    public SkillContext(
            RegistryEntry<Skill> skill,
            LivingEntity source
    ) {
        this.skill = Objects.requireNonNull(skill);
        this.source = Objects.requireNonNull(source);
    }

    public RegistryEntry<Skill> getSkill() {
        return skill;
    }

    public LivingEntity getSource() {
        return source;
    }
}
