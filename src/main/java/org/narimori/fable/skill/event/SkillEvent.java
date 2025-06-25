package org.narimori.fable.skill.event;

import org.narimori.fable.core.event.Event;
import org.narimori.fable.skill.SkillContext;

public interface SkillEvent extends Event {
    SkillContext getContext();
}
