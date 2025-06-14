package org.narimori.fable.channeling;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.narimori.fable.core.attribute.AttributeContainer;

import java.util.Objects;

public final class ChannelingContext {
    private final @NotNull AttributeContainer attributes = AttributeContainer.create();
    private final @NotNull Channeling channeling;
    private final @NotNull LivingEntity source;
    private int duration;

    @ApiStatus.Internal
    public ChannelingContext(
            @NotNull Channeling channeling,
            @NotNull LivingEntity source,
            int duration
    ) {
        this.channeling = Objects.requireNonNull(channeling, "channeling must not be null");
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.duration = duration;
    }

    public @NotNull AttributeContainer getAttributes() {
        return attributes;
    }

    public @NotNull Channeling getChanneling() {
        return channeling;
    }

    public @NotNull LivingEntity getSource() {
        return source;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
