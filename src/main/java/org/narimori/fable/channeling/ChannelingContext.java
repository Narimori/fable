package org.narimori.fable.channeling;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.narimori.fable.core.attribute.AttributeBag;

import java.util.Objects;

public final class ChannelingContext {
    private final Channeling channeling;
    private final LivingEntity source;
    private final AttributeBag attributes;
    private int duration;

    @ApiStatus.Internal
    public ChannelingContext(
            Channeling channeling,
            LivingEntity source,
            AttributeBag attributes,
            int duration
    ) {
        this.channeling = Objects.requireNonNull(channeling);
        this.source = Objects.requireNonNull(source);
        this.attributes = Objects.requireNonNull(attributes);
        this.duration = duration;
    }

    public Channeling getChanneling() {
        return channeling;
    }

    public LivingEntity getSource() {
        return source;
    }

    public AttributeBag getAttributes() {
        return attributes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
