package org.narimori.fable.channeling;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.core.event.EventListener;
import org.narimori.fable.core.event.dispatcher.EventDispatcher;
import org.narimori.fable.entity.attribute.AttributeModifiers;

import java.util.Objects;

public final class Channeling {
    private final @NotNull EventDispatcher<ChannelingEvent> eventDispatcher;
    private final @NotNull AttributeModifiers attributeModifiers;
    private final int duration;

    private Channeling(
            @NotNull EventDispatcher<ChannelingEvent> eventDispatcher,
            @NotNull AttributeModifiers attributeModifiers,
            int duration
    ) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher, "eventDispatcher must not be null");
        this.attributeModifiers = Objects.requireNonNull(attributeModifiers, "attributeModifiers must not be null");
        this.duration = duration;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public @NotNull EventDispatcher<ChannelingEvent> getEventDispatcher() {
        return eventDispatcher;
    }

    public @NotNull AttributeModifiers getAttributeModifiers() {
        return attributeModifiers;
    }

    public int getDuration() {
        return duration;
    }

    public static final class Builder {
        private @NotNull EventDispatcher<ChannelingEvent> eventDispatcher = EventDispatcher.create();
        private @NotNull AttributeModifiers attributeModifiers = AttributeModifiers.create();
        private int duration = 0;

        private Builder() {
        }

        public @NotNull EventDispatcher<ChannelingEvent> getEventDispatcher() {
            return eventDispatcher;
        }

        public @NotNull Builder setEventDispatcher(@NotNull EventDispatcher<ChannelingEvent> eventDispatcher) {
            this.eventDispatcher = Objects.requireNonNull(eventDispatcher, "eventDispatcher must not be null");
            return this;
        }

        public <T extends ChannelingEvent> @NotNull Builder addListener(
                @NotNull Class<T> eventClass,
                @NotNull EventListener<T> eventListener
        ) {
            Objects.requireNonNull(eventClass, "eventClass must not be null");
            Objects.requireNonNull(eventListener, "eventListener must not be null");
            this.eventDispatcher = this.eventDispatcher.addListener(eventClass, eventListener);
            return this;
        }

        public <T extends ChannelingEvent> @NotNull Builder removeListener(
                @NotNull Class<T> eventClass,
                @NotNull EventListener<T> eventListener
        ) {
            Objects.requireNonNull(eventClass, "eventClass must not be null");
            Objects.requireNonNull(eventListener, "eventListener must not be null");
            this.eventDispatcher = this.eventDispatcher.removeListener(eventClass, eventListener);
            return this;
        }

        public @NotNull AttributeModifiers getAttributeModifiers() {
            return attributeModifiers;
        }

        public @NotNull Builder setAttributeModifiers(@NotNull AttributeModifiers attributeModifiers) {
            this.attributeModifiers = Objects.requireNonNull(attributeModifiers, "attributeModifiers must not be null");
            return this;
        }

        public @NotNull Builder addAttributeModifier(
                @NotNull RegistryEntry<EntityAttribute> attribute,
                @NotNull EntityAttributeModifier modifier
        ) {
            Objects.requireNonNull(attribute, "attribute must not be null");
            Objects.requireNonNull(modifier, "modifier must not be null");
            this.attributeModifiers = attributeModifiers.addAttributeModifier(attribute, modifier);
            return this;
        }

        public @NotNull Builder addAttributeModifier(
                @NotNull RegistryEntry<EntityAttribute> attribute,
                @NotNull Identifier id,
                double value,
                @NotNull EntityAttributeModifier.Operation operation
        ) {
            Objects.requireNonNull(attribute, "attribute must not be null");
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(operation, "operation must not be null");
            this.attributeModifiers = attributeModifiers.addAttributeModifier(attribute, id, value, operation);
            return this;
        }

        public int getDuration() {
            return duration;
        }

        public @NotNull Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public @NotNull Channeling build() {
            return new Channeling(eventDispatcher, attributeModifiers, duration);
        }
    }
}
