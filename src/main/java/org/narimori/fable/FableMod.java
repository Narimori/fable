package org.narimori.fable;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.narimori.fable.command.SkillCommand;
import org.narimori.fable.registry.FableRegistries;
import org.narimori.fable.registry.FableRegistryKeys;

public final class FableMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                SkillCommand.register(dispatcher)
        );
        FableRegistries.initialize();
        FableRegistryKeys.initialize();
    }
}
