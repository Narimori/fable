package org.narimori.fable.skill;

import net.minecraft.text.Text;

import java.util.Objects;

public interface SkillResponse {
    static Success success() {
        return new Success();
    }

    static Failure failure(Text reason) {
        return new Failure(reason);
    }

    static Failure onCooldown() {
        return failure(Text.translatable("skill.failure.onCooldown"));
    }

    static Failure notLearned() {
        return failure(Text.translatable("skill.failure.notLearned"));
    }

    static Failure channeling() {
        return failure(Text.translatable("skill.failure.channeling"));
    }

    static Failure unavailable() {
        return failure(Text.translatable("skill.failure.unavailable"));
    }

    final class Success implements SkillResponse {
        private Success() {
        }
    }

    final class Failure implements SkillResponse {
        private final Text reason;

        private Failure(Text reason) {
            this.reason = Objects.requireNonNull(reason);
        }

        public Text getReason() {
            return reason;
        }
    }
}
