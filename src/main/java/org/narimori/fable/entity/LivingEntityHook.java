package org.narimori.fable.entity;

import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.manager.ChannelingManager;
import org.narimori.fable.skill.manager.SkillManager;

public interface LivingEntityHook {
    default @NotNull ChannelingManager getChannelingManager() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    default @NotNull SkillManager getSkillManager() {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
}
