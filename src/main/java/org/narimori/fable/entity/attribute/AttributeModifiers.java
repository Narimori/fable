package org.narimori.fable.entity.attribute;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class AttributeModifiers {
    private final @NotNull Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeModifiers;

    private AttributeModifiers(
            @NotNull Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeModifiers
    ) {
        this.attributeModifiers = Set.copyOf(
                Objects.requireNonNull(attributeModifiers, "attributeModifiers must not be null")
        );
    }

    public static @NotNull AttributeModifiers create() {
        return new AttributeModifiers(Set.of());
    }

    public void addModifiers(@NotNull AttributeContainer container) {
        Objects.requireNonNull(container, "container must not be null");
        for (var entry : attributeModifiers) {
            var instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
                instance.addTemporaryModifier(entry.getValue());
            }
        }
    }

    public void removeModifiers(@NotNull AttributeContainer container) {
        Objects.requireNonNull(container, "container must not be null");
        for (var entry : attributeModifiers) {
            var instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
            }
        }
    }

    public @NotNull AttributeModifiers addAttributeModifier(
            @NotNull RegistryEntry<EntityAttribute> attribute,
            @NotNull EntityAttributeModifier modifier
    ) {
        Objects.requireNonNull(attribute, "attribute must not be null");
        Objects.requireNonNull(modifier, "modifier must not be null");

        Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> newSet =
                new HashSet<>(attributeModifiers.size() + 1);

        newSet.addAll(attributeModifiers);
        newSet.add(Map.entry(attribute, modifier));

        return new AttributeModifiers(newSet);
    }

    public @NotNull AttributeModifiers addAttributeModifier(
            @NotNull RegistryEntry<EntityAttribute> attribute,
            @NotNull Identifier id,
            double value,
            @NotNull EntityAttributeModifier.Operation operation
    ) {
        Objects.requireNonNull(attribute, "attribute must not be null");
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(operation, "operation must not be null");

        return addAttributeModifier(attribute, new EntityAttributeModifier(id, value, operation));
    }
}
