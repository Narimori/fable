package org.narimori.fable.entity.attribute;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class AttributeModifiers {
    private final Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeModifiers;

    private AttributeModifiers(Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeModifiers) {
        this.attributeModifiers = Set.copyOf(Objects.requireNonNull(attributeModifiers));
    }

    public static AttributeModifiers empty() {
        return new AttributeModifiers(Set.of());
    }

    public AttributeModifiers addModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(modifier);

        Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry = Map.entry(attribute, modifier);
        if (attributeModifiers.contains(entry)) {
            return this;
        }

        Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> newSet = new HashSet<>(attributeModifiers);
        newSet.add(entry);
        return new AttributeModifiers(newSet);
    }

    public AttributeModifiers removeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) {
        Objects.requireNonNull(attribute);
        Objects.requireNonNull(modifier);

        Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry = Map.entry(attribute, modifier);
        if (!attributeModifiers.contains(entry)) {
            return this;
        }

        Set<Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> newSet = new HashSet<>(attributeModifiers);
        newSet.remove(entry);
        return new AttributeModifiers(newSet);
    }

    public void addModifiers(AttributeContainer container) {
        Objects.requireNonNull(container);

        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : attributeModifiers) {
            EntityAttributeInstance instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
                instance.addTemporaryModifier(entry.getValue());
            }
        }
    }

    public void removeModifiers(AttributeContainer container) {
        Objects.requireNonNull(container);

        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : attributeModifiers) {
            EntityAttributeInstance instance = container.getCustomInstance(entry.getKey());
            if (instance != null) {
                instance.removeModifier(entry.getValue());
            }
        }
    }
}
