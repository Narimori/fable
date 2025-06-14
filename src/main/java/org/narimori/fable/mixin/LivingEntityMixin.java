package org.narimori.fable.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.channeling.event.entity.ChannelingPostDamageTakenEvent;
import org.narimori.fable.channeling.event.entity.ChannelingPreDamageTakenEvent;
import org.narimori.fable.channeling.manager.ChannelingManager;
import org.narimori.fable.entity.LivingEntityHook;
import org.narimori.fable.skill.event.entity.SkillPostDamageTakenEvent;
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
    private final @NotNull ChannelingManager fable$channelingManager = new ChannelingManager(this::fable$livingEntity);
    @Unique
    private final @NotNull SkillManager fable$skillManager = new SkillManager(this::fable$livingEntity);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @NotNull ChannelingManager getChannelingManager() {
        return fable$channelingManager;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public @NotNull SkillManager getSkillManager() {
        return fable$skillManager;
    }

    @Shadow
    public abstract boolean isDead();

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void fable$tick(@NotNull CallbackInfo ci) {
        fable$skillManager.afterTick();
        fable$channelingManager.afterTick();
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.isSleeping()Z"), cancellable = true)
    private void fable$beforeDamage(
            @NotNull ServerWorld world,
            @NotNull DamageSource source,
            float amount,
            @NotNull CallbackInfoReturnable<Boolean> cir
    ) {
        fable$channelingManager.dispatchChannelingEvent(
                context -> new ChannelingPreDamageTakenEvent(context, source, amount),
                event -> {
                    if (event.isCancelled()) {
                        cir.setReturnValue(false);
                    }
                }
        );
        fable$skillManager.dispatchSkillEvent(
                context -> new SkillPreDamageTakenEvent(context, source, amount),
                event -> {
                    if (event.isCancelled()) {
                        cir.setReturnValue(false);
                    }
                }
        );
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void fable$afterDamage(
            @NotNull ServerWorld world,
            @NotNull DamageSource source,
            float amount,
            @NotNull CallbackInfoReturnable<Boolean> cir,
            @Local(ordinal = 1) float dealt,
            @Local(ordinal = 0) boolean blocked
    ) {
        if (isDead()) {
            return;
        }

        fable$channelingManager.dispatchChannelingEvent(
                context -> new ChannelingPostDamageTakenEvent(context, source, dealt, amount, blocked),
                event -> {
                }
        );
        fable$skillManager.dispatchSkillEvent(
                context -> new SkillPostDamageTakenEvent(context, source, dealt, amount, blocked),
                event -> {
                }
        );
    }

    @Unique
    private @NotNull LivingEntity fable$livingEntity() {
        return (LivingEntity) (Object) this;
    }
}
