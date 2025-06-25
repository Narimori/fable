package org.narimori.fable.entity;

import org.narimori.fable.channeling.manager.ChannelingManager;
import org.narimori.fable.skill.manager.SkillManager;

public interface LivingEntityHook {
    default ChannelingManager getChannelingManager() {
        throw new UnsupportedOperationException();
    }

    default SkillManager getSkillManager() {
        throw new UnsupportedOperationException();
    }
}
