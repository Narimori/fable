package org.narimori.fable.skill.action;

import org.jetbrains.annotations.ApiStatus;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;

@FunctionalInterface
public interface SkillAction {
    static SkillAction alwaysSuccess() {
        return context -> SkillResponse.success();
    }

    SkillResponse execute(SkillContext context);

    @ApiStatus.NonExtendable
    default SkillAction and(SkillAction other) {
        return context -> execute(context) instanceof SkillResponse.Failure failure ?
                failure :
                other.execute(context);
    }

    @ApiStatus.NonExtendable
    default SkillAction or(SkillAction other) {
        return context -> execute(context) instanceof SkillResponse.Success success ?
                success :
                other.execute(context);
    }
}
