package org.narimori.fable.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.skill.Skill;

public final class FableRegistries {
    public static final @NotNull Registry<Skill> SKILL = FabricRegistryBuilder.createSimple(FableRegistryKeys.SKILL).buildAndRegister();

    private FableRegistries() {
    }

    public static void init() {
    }
}
