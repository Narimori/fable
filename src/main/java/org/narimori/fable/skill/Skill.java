package org.narimori.fable.skill;

import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.core.event.EventListener;
import org.narimori.fable.core.event.dispatcher.EventDispatcher;
import org.narimori.fable.registry.FableRegistries;
import org.narimori.fable.skill.action.SkillAction;
import org.narimori.fable.skill.condition.SkillCondition;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class Skill {
    private final @NotNull EventDispatcher<SkillEvent> eventDispatcher;
    private final @NotNull SkillAction action;
    private final @NotNull SkillCondition condition;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(
            @NotNull EventDispatcher<SkillEvent> eventDispatcher,
            @NotNull SkillAction action,
            @NotNull SkillCondition condition
    ) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher, "eventDispatcher must not be null");
        this.action = Objects.requireNonNull(action, "action must not be null");
        this.condition = Objects.requireNonNull(condition, "condition must not be null");
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public @NotNull EventDispatcher<SkillEvent> getEventDispatcher() {
        return eventDispatcher;
    }

    public @NotNull SkillAction getAction() {
        return action;
    }

    public @NotNull SkillCondition getCondition() {
        return condition;
    }

    public @NotNull String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("skill", FableRegistries.SKILL.getId(this));
        }

        return translationKey;
    }

    public @NotNull Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder {
        private @NotNull EventDispatcher<SkillEvent> eventDispatcher = EventDispatcher.create();
        private @NotNull SkillAction action = SkillAction.alwaysSuccess();
        private @NotNull SkillCondition condition = SkillCondition.defaultConditions();

        private Builder() {
        }

        public @NotNull EventDispatcher<SkillEvent> getEventDispatcher() {
            return eventDispatcher;
        }

        public @NotNull Builder setEventDispatcher(@NotNull EventDispatcher<SkillEvent> eventDispatcher) {
            this.eventDispatcher = eventDispatcher;
            return this;
        }

        public <T extends SkillEvent> @NotNull Builder addListener(
                @NotNull Class<T> eventClass,
                @NotNull EventListener<T> eventListener
        ) {
            Objects.requireNonNull(eventClass, "eventClass must not be null");
            Objects.requireNonNull(eventListener, "eventListener must not be null");
            this.eventDispatcher = this.eventDispatcher.addListener(eventClass, eventListener);
            return this;
        }

        public <T extends SkillEvent> @NotNull Builder removeListener(
                @NotNull Class<T> eventClass,
                @NotNull EventListener<T> eventListener
        ) {
            Objects.requireNonNull(eventClass, "eventClass must not be null");
            Objects.requireNonNull(eventListener, "eventListener must not be null");
            this.eventDispatcher = this.eventDispatcher.removeListener(eventClass, eventListener);
            return this;
        }

        public @NotNull SkillAction getAction() {
            return action;
        }

        public @NotNull Builder setAction(@NotNull SkillAction action) {
            this.action = Objects.requireNonNull(action, "action must not be null");
            return this;
        }

        public @NotNull SkillCondition getCondition() {
            return condition;
        }

        public @NotNull Builder setCondition(@NotNull SkillCondition condition) {
            this.condition = Objects.requireNonNull(condition, "condition must not be null");
            return this;
        }

        public @NotNull Skill build() {
            return new Skill(eventDispatcher, action, condition);
        }
    }
}
