package org.narimori.fable.skill;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class SkillContext {
    private final @NotNull RegistryEntry<Skill> skill;
    private final @NotNull LivingEntity source;

    @ApiStatus.Internal
    public SkillContext(
            @NotNull RegistryEntry<Skill> skill,
            @NotNull LivingEntity source
    ) {
        this.skill = Objects.requireNonNull(skill, "skill must not be null");
        this.source = Objects.requireNonNull(source, "source must not be null");
    }

    public @NotNull RegistryEntry<Skill> getSkill() {
        return skill;
    }

    public @NotNull LivingEntity getSource() {
        return source;
    }
}
