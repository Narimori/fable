package org.narimori.fable.skill;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.narimori.fable.registry.FableRegistries;
import org.narimori.fable.skill.condition.SkillCondition;
import org.narimori.fable.skill.event.entity.SkillPostDamageTakenEvent;
import org.narimori.fable.skill.event.entity.SkillPreDamageTakenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SkillTest implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkillTest.class);

    private final Skill testSkill = Skill.builder()
            .addListener(SkillPreDamageTakenEvent.class, event ->
                    LOGGER.info("Skill pre damaged ({})", event.getContext().getSkill().getIdAsString())
            )
            .addListener(SkillPostDamageTakenEvent.class, event ->
                    LOGGER.info("Skill post damaged: ({})", event.getContext().getSkill().getIdAsString())
            )
            .setCondition(context -> {
                SkillResponse response = SkillCondition.defaultConditions().check(context);

                LOGGER.info("Skill condition {}", response instanceof SkillResponse.Success ? "succeeded" : "failed");
                return response;
            })
            .setAction(context -> {
                context.getSource().getSkillManager().setSkillCooldown(context.getSkill(), 60);

                LOGGER.info("Skill executed");
                return SkillResponse.success();
            })
            .build();

    @Override
    public void onInitialize() {
        Registry.register(FableRegistries.SKILL, Identifier.of("fable-test", "test"), testSkill);
    }
}
