package org.narimori.fable.channeling;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.narimori.fable.channeling.event.entity.ChannelingPostDamageDealtEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPostDamageTakenEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPreDamageDealtEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPreDamageTakenEvent;
import org.narimori.fable.channeling.event.lifecycle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChannelingTest implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelingTest.class);

    private final Channeling testChanneling = Channeling.builder()
            .setAction(context ->
                    LOGGER.info("Channeling executed: {}", context.getDuration())
            )
            .addModifier(
                    EntityAttributes.MOVEMENT_SPEED,
                    new EntityAttributeModifier(
                            Identifier.of("fable-test", "channeling"),
                            -0.7,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            )
            .addListener(ChannelingPreDamageDealtEvent.class, event ->
                    LOGGER.info("Channeling pre damage dealt: {}", event.getContext().getSource().getName().getString())
            )
            .addListener(ChannelingPostDamageDealtEvent.class, event ->
                    LOGGER.info("Channeling post damage dealt: {}", event.getContext().getSource().getName().getString())
            )
            .addListener(ChannelingPreDamageTakenEvent.class, event ->
                    LOGGER.info("Channeling pre damage taken: {}", event.getContext().getSource().getName().getString())
            )
            .addListener(ChannelingPostDamageTakenEvent.class, event ->
                    LOGGER.info("Channeling post damage taken: {}", event.getContext().getSource().getName().getString())
            )
            .addListener(ChannelingCancelEvent.class, event ->
                    LOGGER.info("Channeling cancelled: {}", event.getContext().getDuration())
            )
            .addListener(ChannelingCompleteEvent.class, event -> {
                LOGGER.info("Channeling completed: {}", event.getContext().getDuration());

                // attribute test
                Vec3d rotationVector = event.getContext().getAttributes().getOrThrow("rotationVector");
                event.getContext().getSource().setVelocity(rotationVector);
                event.getContext().getSource().velocityModified = true;
            })
            .addListener(ChannelingEndEvent.class, event ->
                    LOGGER.info("Channeling ended: {}", event.getContext().getDuration())
            )
            .addListener(ChannelingInterruptEvent.class, event ->
                    LOGGER.info("Channeling interrupted: {}", event.getContext().getDuration())
            )
            .addListener(ChannelingStartEvent.class, event -> {
                LOGGER.info("Channeling started: {}", event.getContext().getDuration());

                // attribute test
                Vec3d rotationVector = event.getContext().getSource().getRotationVector();
                event.getContext().getAttributes().set("rotationVector", rotationVector);
            })
            .addListener(ChannelingTickEvent.class, event ->
                    LOGGER.info("Channeling ticked: {}", event.getContext().getDuration())
            )
            .setDuration(20)
            .build();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(
                        CommandManager.literal("channelingtest")
                                .then(
                                        CommandManager.literal("test")
                                                .executes(context -> {
                                                    LOGGER.info(
                                                            "Channeling test {}",
                                                            ((LivingEntity) context.getSource().getEntityOrThrow()).getChannelingManager()
                                                                    .isChanneling() ?
                                                                    "succeeded" :
                                                                    "failed"
                                                    );
                                                    return 1;
                                                })
                                )
                                .then(
                                        CommandManager.literal("start")
                                                .executes(context -> {
                                                    LOGGER.info(
                                                            "Channeling start {}",
                                                            ((LivingEntity) context.getSource().getEntityOrThrow()).getChannelingManager()
                                                                    .startChanneling(testChanneling) ?
                                                                    "succeeded" :
                                                                    "failed"
                                                    );
                                                    return 1;
                                                })
                                )
                                .then(
                                        CommandManager.literal("cancel")
                                                .executes(context -> {
                                                    LOGGER.info(
                                                            "Channeling cancel {}",
                                                            ((LivingEntity) context.getSource().getEntityOrThrow()).getChannelingManager()
                                                                    .cancelChanneling() ?
                                                                    "succeeded" :
                                                                    "failed"
                                                    );
                                                    return 1;
                                                })
                                )
                                .then(
                                        CommandManager.literal("interrupt")
                                                .executes(context -> {
                                                    LOGGER.info(
                                                            "Channeling interrupt {}",
                                                            ((LivingEntity) context.getSource().getEntityOrThrow()).getChannelingManager()
                                                                    .interruptChanneling() ?
                                                                    "succeeded" :
                                                                    "failed"
                                                    );
                                                    return 1;
                                                })
                                )
                )
        );
    }
}
