package org.narimori.fable.skill;

import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.core.event.EventListener;
import org.narimori.fable.core.event.dispatch.EventDispatcher;
import org.narimori.fable.registry.FableRegistries;
import org.narimori.fable.skill.action.SkillAction;
import org.narimori.fable.skill.condition.SkillCondition;
import org.narimori.fable.skill.event.SkillEvent;

import java.util.Objects;

public final class Skill {
    private final SkillAction action;
    private final SkillCondition condition;
    private final EventDispatcher<SkillEvent> eventDispatcher;

    private @Nullable String translationKey;
    private @Nullable Text name;

    private Skill(
            SkillAction action,
            SkillCondition condition,
            EventDispatcher<SkillEvent> eventDispatcher
    ) {
        this.action = Objects.requireNonNull(action);
        this.condition = Objects.requireNonNull(condition);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
    }

    public static Builder builder() {
        return new Builder();
    }

    public SkillAction getAction() {
        return action;
    }

    public SkillCondition getCondition() {
        return condition;
    }

    public EventDispatcher<SkillEvent> getEventDispatcher() {
        return eventDispatcher;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("skill", FableRegistries.SKILL.getId(this));
        }

        return translationKey;
    }

    public Text getName() {
        if (name == null) {
            name = Text.translatable(getTranslationKey());
        }

        return name;
    }

    public static final class Builder {
        private SkillAction action = SkillAction.alwaysSuccess();
        private SkillCondition condition = SkillCondition.defaultConditions();
        private EventDispatcher<SkillEvent> eventDispatcher = EventDispatcher.empty();

        private Builder() {
        }

        public SkillAction getAction() {
            return action;
        }

        public Builder setAction(SkillAction action) {
            this.action = Objects.requireNonNull(action);
            return this;
        }

        public SkillCondition getCondition() {
            return condition;
        }

        public Builder setCondition(SkillCondition condition) {
            this.condition = Objects.requireNonNull(condition);
            return this;
        }

        public EventDispatcher<SkillEvent> getEventDispatcher() {
            return eventDispatcher;
        }

        public Builder setEventDispatcher(EventDispatcher<SkillEvent> eventDispatcher) {
            this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
            return this;
        }

        public <S extends SkillEvent> Builder addListener(Class<S> eventClass, EventListener<S> eventListener) {
            Objects.requireNonNull(eventClass);
            Objects.requireNonNull(eventListener);

            eventDispatcher = eventDispatcher.addListener(eventClass, eventListener);
            return this;
        }

        public <S extends SkillEvent> Builder removeListener(Class<S> eventClass, EventListener<S> eventListener) {
            Objects.requireNonNull(eventClass);
            Objects.requireNonNull(eventListener);

            eventDispatcher = eventDispatcher.removeListener(eventClass, eventListener);
            return this;
        }

        public Skill build() {
            return new Skill(action, condition, eventDispatcher);
        }
    }
}
