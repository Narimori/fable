package org.narimori.fable.skill.event;

import org.jetbrains.annotations.NotNull;
import org.narimori.fable.core.event.Event;
import org.narimori.fable.skill.SkillContext;

import java.util.Objects;

public abstract class SkillEvent implements Event {
    private final @NotNull SkillContext context;

    protected SkillEvent(@NotNull SkillContext context) {
        this.context = Objects.requireNonNull(context, "context must not be null");
    }

    public @NotNull SkillContext getContext() {
        return context;
    }
}
