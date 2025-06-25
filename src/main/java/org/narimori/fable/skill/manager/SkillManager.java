package org.narimori.fable.skill.manager;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.ApiStatus;
import org.narimori.fable.skill.Skill;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SkillManager {
    private final Supplier<LivingEntity> entitySupplier;

    private final Map<RegistryEntry<Skill>, Integer> skillCooldowns = new HashMap<>();
    private final Set<RegistryEntry<Skill>> skills = new HashSet<>();

    @ApiStatus.Internal
    public SkillManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean hasSkillCooldown(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return getSkillCooldown(skill) > 0;
    }

    public int getSkillCooldown(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skillCooldowns.getOrDefault(skill, 0);
    }

    public void setSkillCooldown(RegistryEntry<Skill> skill, int cooldown) {
        Objects.requireNonNull(skill);
        skillCooldowns.put(skill, cooldown);
    }

    public boolean hasSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skills.contains(skill);
    }

    public Set<? extends RegistryEntry<Skill>> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public boolean addSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skills.add(skill);
    }

    public boolean removeSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skills.remove(skill);
    }

    public SkillResponse canUseSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return skill.value().getCondition().test(skillContext(skill));
    }

    public SkillResponse useSkill(RegistryEntry<Skill> skill) {
        Objects.requireNonNull(skill);
        return canUseSkill(skill) instanceof SkillResponse.Failure failure ?
                failure :
                skill.value().getAction().execute(skillContext(skill));
    }

    public boolean dispatchSkillEvent(Function<SkillContext, SkillEvent> mapper) {
        return skills.stream().allMatch(skill ->
                skill.value()
                        .getEventDispatcher()
                        .dispatch(mapper.apply(skillContext(skill)))
        );
    }

    @ApiStatus.Internal
    public void update() {
        updateSkillCooldowns();
    }

    private void updateSkillCooldowns() {
        Iterator<Map.Entry<RegistryEntry<Skill>, Integer>> iterator = skillCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RegistryEntry<Skill>, Integer> entry = iterator.next();
            int cooldown = entry.getValue() - 1;
            if (cooldown > 0) {
                entry.setValue(cooldown);
            } else {
                iterator.remove();
            }
        }
    }

    private SkillContext skillContext(RegistryEntry<Skill> skill) {
        return new SkillContext(skill, entitySupplier.get());
    }
}
