package org.narimori.fable.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Identifier;
import org.narimori.fable.registry.FableRegistries;
import org.narimori.fable.skill.Skill;
import org.narimori.fable.skill.SkillResponse;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class SkillCommand {
    private static final DynamicCommandExceptionType UNKNOWN_SKILL_EXCEPTION = new DynamicCommandExceptionType(id ->
            Text.stringifiedTranslatable("skill.notFound", id)
    );
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(entityName ->
            Text.translatable("commands.skill.failed.entity", entityName)
    );
    private static final Dynamic2CommandExceptionType ADD_FAILURE_EXCEPTION = new Dynamic2CommandExceptionType((entityName, skillName) ->
            Text.translatable("commands.skill.add.failure", entityName, skillName)
    );
    private static final Dynamic2CommandExceptionType REMOVE_FAILURE_EXCEPTION = new Dynamic2CommandExceptionType((entityName, skillName) ->
            Text.translatable("commands.skill.remove.failure", entityName, skillName)
    );
    private static final Dynamic3CommandExceptionType TEST_FAILURE_EXCEPTION = new Dynamic3CommandExceptionType((entityName, skillName, reason) ->
            Text.translatable("commands.skill.test.failure", entityName, skillName, reason)
    );
    private static final Dynamic3CommandExceptionType USE_FAILURE_EXCEPTION = new Dynamic3CommandExceptionType((entityName, skillName, reason) ->
            Text.translatable("commands.skill.use.failure", entityName, skillName, reason)
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(argumentSkill());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentSkill() {
        return CommandManager.literal("skill")
                .requires(source -> source.hasPermissionLevel(2))
                .then(argumentGet())
                .then(argumentAdd())
                .then(argumentRemove())
                .then(argumentTest())
                .then(argumentUse());
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentGet() {
        return CommandManager.literal("get")
                .executes(context ->
                        executeGet(
                                context.getSource(),
                                context.getSource().getEntityOrThrow()
                        )
                )
                .then(
                        CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context ->
                                        executeGet(
                                                context.getSource(),
                                                EntityArgumentType.getEntity(context, "target")
                                        )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentAdd() {
        return CommandManager.literal("add")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeAdd(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeAdd(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentRemove() {
        return CommandManager.literal("remove")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeRemove(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeRemove(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentTest() {
        return CommandManager.literal("test")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeTest(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeTest(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static LiteralArgumentBuilder<ServerCommandSource> argumentUse() {
        return CommandManager.literal("use")
                .then(
                        CommandManager.argument("skill", IdentifierArgumentType.identifier())
                                .suggests((context, builder) ->
                                        CommandSource.suggestIdentifiers(
                                                FableRegistries.SKILL.getIds(),
                                                builder
                                        )
                                )
                                .executes(context ->
                                        executeUse(
                                                context.getSource(),
                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                List.of(context.getSource().getEntityOrThrow())
                                        )
                                )
                                .then(
                                        CommandManager.argument("targets", EntityArgumentType.entities())
                                                .executes(context ->
                                                        executeUse(
                                                                context.getSource(),
                                                                getSkill(IdentifierArgumentType.getIdentifier(context, "skill")),
                                                                EntityArgumentType.getEntities(context, "targets")
                                                        )
                                                )
                                )
                );
    }

    private static int executeGet(
            ServerCommandSource source,
            Entity target
    ) throws CommandSyntaxException {
        Set<? extends RegistryEntry<Skill>> skills = getLivingEntity(target).getSkillManager().getSkills();
        if (skills.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.skill.get.empty", target.getName()), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.get.success", target.getName(), skills.size(), Texts.join(skills, entry -> entry.value().getName())), false);
        }

        return 1;
    }

    private static int executeAdd(
            ServerCommandSource source,
            RegistryEntry<Skill> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillManager().addSkill(skill)) {
                throw ADD_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.add.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.add.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeRemove(
            ServerCommandSource source,
            RegistryEntry<Skill> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (!getLivingEntity(target).getSkillManager().removeSkill(skill)) {
                throw REMOVE_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.remove.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.remove.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeTest(
            ServerCommandSource source,
            RegistryEntry<Skill> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (getLivingEntity(target).getSkillManager().canUseSkill(skill) instanceof SkillResponse.Failure failure) {
                throw TEST_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName(), failure.getReason());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.test.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.test.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeUse(
            ServerCommandSource source,
            RegistryEntry<Skill> skill,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        for (Entity target : targets) {
            if (getLivingEntity(target).getSkillManager().useSkill(skill) instanceof SkillResponse.Failure failure) {
                throw USE_FAILURE_EXCEPTION.create(target.getName(), skill.value().getName(), failure.getReason());
            }
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.skill.use.success.single", targets.iterator().next().getName(), skill.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.skill.use.success.multiple", targets.size(), skill.value().getName()), true);
        }

        return targets.size();
    }

    private static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(entity.getName());
        }
    }

    private static RegistryEntry<Skill> getSkill(Identifier id) throws CommandSyntaxException {
        return FableRegistries.SKILL.getEntry(id)
                .orElseThrow(() -> UNKNOWN_SKILL_EXCEPTION.create(id));
    }
}
