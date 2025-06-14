package org.narimori.fable.skill.condition;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;

import java.util.Objects;

@FunctionalInterface
public interface SkillCondition {
    static @NotNull SkillCondition alwaysSuccess() {
        return context -> SkillResponse.success();
    }

    static @NotNull SkillCondition requireCooldownReady() {
        return context -> context.getSource().getSkillManager().hasSkillCooldown(context.getSkill())
                ? SkillResponse.onCooldown()
                : SkillResponse.success();
    }

    static @NotNull SkillCondition requireSkillLearned() {
        return context -> context.getSource().getSkillManager().hasSkill(context.getSkill())
                ? SkillResponse.success()
                : SkillResponse.notLearned();
    }

    static @NotNull SkillCondition requireNotChanneling() {
        return context -> context.getSource().getChannelingManager().isChanneling()
                ? SkillResponse.channeling()
                : SkillResponse.success();
    }

    static @NotNull SkillCondition requireInGame() {
        return context -> context.getSource().isPartOfGame()
                ? SkillResponse.success()
                : SkillResponse.unavailable();
    }

    static @NotNull SkillCondition defaultConditions() {
        return requireCooldownReady()
                .and(requireSkillLearned())
                .and(requireNotChanneling())
                .and(requireInGame());
    }

    @NotNull SkillResponse check(@NotNull SkillContext context);

    @ApiStatus.NonExtendable
    default @NotNull SkillCondition and(@NotNull SkillCondition other) {
        Objects.requireNonNull(other, "other must not be null");
        return context -> check(context) instanceof SkillResponse.Failure failure ?
                failure :
                other.check(context);
    }

    @ApiStatus.NonExtendable
    default @NotNull SkillCondition or(@NotNull SkillCondition other) {
        Objects.requireNonNull(other, "other must not be null");
        return context -> check(context) instanceof SkillResponse.Success success ?
                success :
                other.check(context);
    }
}
