package org.narimori.fable.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.Skill;

public final class FableRegistryKeys {
    public static final @NotNull RegistryKey<Registry<Skill>> SKILL = RegistryKey.ofRegistry(Identifier.of("fable", "skill"));

    private FableRegistryKeys() {
    }

    public static void init() {
    }
}
