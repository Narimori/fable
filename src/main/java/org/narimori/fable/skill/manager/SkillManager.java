package org.narimori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.Skill;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SkillManager {
    private final @NotNull Supplier<@NotNull LivingEntity> entitySupplier;
    
    private final @NotNull Map<RegistryEntry<Skill>, Integer> skillCooldowns = new HashMap<>();
    private final @NotNull Set<RegistryEntry<Skill>> skills = new HashSet<>();

    @ApiStatus.Internal
    public SkillManager(@NotNull Supplier<@NotNull LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier, "entitySupplier must not be null");
    }

    public boolean hasSkillCooldown(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return getSkillCooldown(skill) > 0;
    }

    public int getSkillCooldown(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return skillCooldowns.getOrDefault(skill, 0);
    }

    public void setSkillCooldown(@NotNull RegistryEntry<Skill> skill, int cooldown) {
        Objects.requireNonNull(skill, "skill must not be null");
        skillCooldowns.put(skill, cooldown);
    }

    public boolean hasSkill(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return skills.contains(skill);
    }

    public @NotNull Set<? extends RegistryEntry<Skill>> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public boolean addSkill(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return skills.add(skill);
    }

    public boolean removeSkill(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return skills.remove(skill);
    }

    public @NotNull SkillResponse canUseSkill(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return skill.value().getCondition().check(skillContext(skill));
    }

    public @NotNull SkillResponse useSkill(@NotNull RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill, "skill must not be null");
        return canUseSkill(skill) instanceof SkillResponse.Failure failure ?
                failure :
                skill.value().getAction().execute(skillContext(skill));
    }

    public <T extends SkillEvent> void dispatchSkillEvent(
            @NotNull Function<@NotNull SkillContext, @NotNull T> mapper,
            @NotNull Consumer<@NotNull T> afterAction
    ) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        Objects.requireNonNull(afterAction, "afterAction must not be null");

        for (RegistryEntry<Skill> skill : skills) {
            T event = mapper.apply(skillContext(skill));

            skill.value().getEventDispatcher().dispatch(event);
            afterAction.accept(event);
        }
    }

    @ApiStatus.Internal
    public void afterTick() {
        skillCooldowns.replaceAll((skill, cooldown) -> cooldown - 1);
        skillCooldowns.entrySet().removeIf(entry -> entry.getValue() <= 0);
    }
    
    private @NotNull SkillContext skillContext(@NotNull RegistryEntry<Skill> skill) {
        return new SkillContext(skill, entitySupplier.get());
    }
}
