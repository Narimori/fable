package org.narimori.fable.skill;

import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public sealed interface SkillResponse {
    static @NotNull Success success() {
        return new Success();
    }

    static @NotNull Failure onCooldown() {
        return failure(Text.translatable("skill.failure.onCooldown"));
    }

    static @NotNull Failure notLearned() {
        return failure(Text.translatable("skill.failure.notLearned"));
    }

    static @NotNull Failure channeling() {
        return failure(Text.translatable("skill.failure.channeling"));
    }

    static @NotNull Failure unavailable() {
        return failure(Text.translatable("skill.failure.unavailable"));
    }

    static @NotNull Failure failure(@NotNull Text reason) {
        return new Failure(reason);
    }

    final class Success implements SkillResponse {
        private Success() {
        }
    }

    final class Failure implements SkillResponse {
        private final @NotNull Text reason;

        private Failure(@NotNull Text reason) {
            this.reason = Objects.requireNonNull(reason, "reason must not be null");
        }

        public @NotNull Text getReason() {
            return reason;
        }
    }
}
