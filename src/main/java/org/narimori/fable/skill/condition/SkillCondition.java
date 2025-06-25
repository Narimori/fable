package org.narimori.fable.skill.condition;

import org.jetbrains.annotations.ApiStatus;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;

@FunctionalInterface
public interface SkillCondition {
    static SkillCondition alwaysSuccess() {
        return context -> SkillResponse.success();
    }

    static SkillCondition requireCooldownReady() {
        return context -> context.getSource().getSkillManager().hasSkillCooldown(context.getSkill()) ?
                SkillResponse.onCooldown() :
                SkillResponse.success();
    }

    static SkillCondition requireSkillLearned() {
        return context -> context.getSource().getSkillManager().hasSkill(context.getSkill()) ?
                SkillResponse.success() :
                SkillResponse.notLearned();
    }

    static SkillCondition requireNotChanneling() {
        return context -> context.getSource().getChannelingManager().isChanneling() ?
                SkillResponse.channeling() :
                SkillResponse.success();
    }

    static SkillCondition requireInGame() {
        return context -> context.getSource().isPartOfGame() ?
                SkillResponse.success() :
                SkillResponse.unavailable();
    }

    static SkillCondition defaultConditions() {
        return requireCooldownReady()
                .and(requireSkillLearned())
                .and(requireNotChanneling())
                .and(requireInGame());
    }

    SkillResponse test(SkillContext context);

    @ApiStatus.NonExtendable
    default SkillCondition and(SkillCondition other) {
        return context -> test(context) instanceof SkillResponse.Failure failure ?
                failure :
                other.test(context);
    }

    @ApiStatus.NonExtendable
    default SkillCondition or(SkillCondition other) {
        return context -> test(context) instanceof SkillResponse.Success success ?
                success :
                other.test(context);
    }
}
