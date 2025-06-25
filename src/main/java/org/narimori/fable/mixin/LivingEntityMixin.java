package org.narimori.fable.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.narimori.fable.channeling.event.entity.ChannelingPostDamageDealtEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPostDamageTakenEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPreDamageDealtEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPreDamageTakenEvent;
import org.narimori.fable.channeling.manager.ChannelingManager;
import org.narimori.fable.entity.LivingEntityHook;
import org.narimori.fable.skill.event.entity.SkillPostDamageDealtEvent;
import org.narimori.fable.skill.event.entity.SkillPostDamageTakenEvent;
import org.narimori.fable.skill.event.entity.SkillPreDamageDealtEvent;
import org.narimori.fable.skill.event.entity.SkillPreDamageTakenEvent;
import org.narimori.fable.skill.manager.SkillManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityHook {
    @Unique
    private final ChannelingManager fable$channelingManager = new ChannelingManager(this::fable$livingEntity);
    @Unique
    private final SkillManager fable$skillManager = new SkillManager(this::fable$livingEntity);

    @Shadow
    public abstract boolean isDead();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public ChannelingManager getChannelingManager() {
        return fable$channelingManager;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public SkillManager getSkillManager() {
        return fable$skillManager;
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void fable$tick(CallbackInfo ci) {
        fable$channelingManager.update();
        fable$skillManager.update();
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.isSleeping()Z"), cancellable = true)
    private void beforeDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = fable$livingEntity();
        if (!fable$channelingManager.dispatchChannelingEvent(context -> new ChannelingPreDamageTakenEvent(entity, source.getAttacker(), source, amount, context)) ||
                !fable$skillManager.dispatchSkillEvent(context -> new SkillPreDamageTakenEvent(entity, source.getAttacker(), source, amount, context))
        ) {
            cir.setReturnValue(false);
            return;
        }

        if (!(source.getAttacker() instanceof LivingEntity attacker)) {
            return;
        }

        if (!attacker.getChannelingManager().dispatchChannelingEvent(context -> new ChannelingPreDamageDealtEvent(entity, attacker, source, amount, context)) ||
                !attacker.getSkillManager().dispatchSkillEvent(context -> new SkillPreDamageDealtEvent(entity, attacker, source, amount, context))
        ) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void afterDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) float dealt, @Local(ordinal = 0) boolean blocked) {
        if (isDead()) {
            return;
        }

        LivingEntity entity = fable$livingEntity();
        fable$channelingManager.dispatchChannelingEvent(context -> new ChannelingPostDamageTakenEvent(entity, source.getAttacker(), source, dealt, amount, blocked, context));
        fable$skillManager.dispatchSkillEvent(context -> new SkillPostDamageTakenEvent(entity, source.getAttacker(), source, dealt, amount, blocked, context));

        if (!(source.getAttacker() instanceof LivingEntity attacker)) {
            return;
        }

        attacker.getChannelingManager().dispatchChannelingEvent(context -> new ChannelingPostDamageDealtEvent(entity, attacker, source, dealt, amount, blocked, context));
        attacker.getSkillManager().dispatchSkillEvent(context -> new SkillPostDamageDealtEvent(entity, attacker, source, dealt, amount, blocked, context));
    }

    @Unique
    private LivingEntity fable$livingEntity() {
        return (LivingEntity) (Object) this;
    }
}
