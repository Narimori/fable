package org.narimori.fable.channeling;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import org.narimori.fable.channeling.action.ChannelingAction;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.EventListener;
import org.narimori.fable.core.event.dispatch.EventDispatcher;
import org.narimori.fable.entity.attribute.AttributeModifiers;

import java.util.Objects;

public final class Channeling {
    private final ChannelingAction action;
    private final AttributeModifiers attributeModifiers;
    private final EventDispatcher<ChannelingEvent> eventDispatcher;
    private final int duration;

    private Channeling(
            ChannelingAction action,
            AttributeModifiers attributeModifiers,
            EventDispatcher<ChannelingEvent> eventDispatcher,
            int duration
    ) {
        this.action = Objects.requireNonNull(action);
        this.attributeModifiers = Objects.requireNonNull(attributeModifiers);
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.duration = duration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ChannelingAction getAction() {
        return action;
    }

    public AttributeModifiers getAttributeModifiers() {
        return attributeModifiers;
    }

    public EventDispatcher<ChannelingEvent> getEventDispatcher() {
        return eventDispatcher;
    }

    public int getDuration() {
        return duration;
    }

    public static final class Builder {
        private ChannelingAction action = ChannelingAction.noOp();
        private AttributeModifiers attributeModifiers = AttributeModifiers.empty();
        private EventDispatcher<ChannelingEvent> eventDispatcher = EventDispatcher.empty();
        private int duration = 0;

        private Builder() {
        }

        public ChannelingAction getAction() {
            return action;
        }

        public Builder setAction(ChannelingAction action) {
            this.action = Objects.requireNonNull(action);
            return this;
        }

        public AttributeModifiers getAttributeModifiers() {
            return attributeModifiers;
        }

        public Builder setAttributeModifiers(AttributeModifiers attributeModifiers) {
            this.attributeModifiers = Objects.requireNonNull(attributeModifiers);
            return this;
        }

        public Builder addModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(modifier);

            attributeModifiers = attributeModifiers.addModifier(attribute, modifier);
            return this;
        }

        public Builder removeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
            Objects.requireNonNull(attribute);
            Objects.requireNonNull(modifier);

            attributeModifiers = attributeModifiers.removeModifier(attribute, modifier);
            return this;
        }

        public EventDispatcher<ChannelingEvent> getEventDispatcher() {
            return eventDispatcher;
        }

        public Builder setEventDispatcher(EventDispatcher<ChannelingEvent> eventDispatcher) {
            this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
            return this;
        }

        public <S extends ChannelingEvent> Builder addListener(Class<S> eventClass, EventListener<S> eventListener) {
            Objects.requireNonNull(eventClass);
            Objects.requireNonNull(eventListener);

            eventDispatcher = eventDispatcher.addListener(eventClass, eventListener);
            return this;
        }

        public <S extends ChannelingEvent> Builder removeListener(Class<S> eventClass, EventListener<S> eventListener) {
            Objects.requireNonNull(eventClass);
            Objects.requireNonNull(eventListener);

            eventDispatcher = eventDispatcher.removeListener(eventClass, eventListener);
            return this;
        }

        public int getDuration() {
            return duration;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Channeling build() {
            return new Channeling(action, attributeModifiers, eventDispatcher, duration);
        }
    }
}
