package org.narimori.fable.skill.action;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.SkillContext;
import org.narimori.fable.skill.SkillResponse;

import java.util.Objects;

@FunctionalInterface
public interface SkillAction {
    static @NotNull SkillAction alwaysSuccess() {
        return context -> SkillResponse.success();
    }

    @NotNull SkillResponse execute(@NotNull SkillContext context);

    @ApiStatus.NonExtendable
    default @NotNull SkillAction and(@NotNull SkillAction other) {
        Objects.requireNonNull(other, "other must not be null");
        return context -> execute(context) instanceof SkillResponse.Failure failure ?
                failure :
                other.execute(context);
    }

    @ApiStatus.NonExtendable
    default @NotNull SkillAction or(@NotNull SkillAction other) {
        Objects.requireNonNull(other, "other must not be null");
        return context -> execute(context) instanceof SkillResponse.Success success ?
                success :
                other.execute(context);
    }
}
