package org.narimori.fable.channeling.manager;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.channeling.Channeling;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.channeling.event.lifecycle.*;
import org.narimori.fable.core.attribute.AttributeBag;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChannelingManager {
    private final Supplier<LivingEntity> entitySupplier;

    private @Nullable ChannelingContext channelingContext;

    @ApiStatus.Internal
    public ChannelingManager(Supplier<LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier);
    }

    public boolean isChanneling() {
        return channelingContext != null;
    }

    public @Nullable ChannelingContext getChannelingContext() {
        return channelingContext;
    }

    public boolean startChanneling(Channeling channeling) {
        Objects.requireNonNull(channeling);
        if (isChanneling()) {
            return false;
        }

        beginChanneling(channelingContext(channeling));
        return true;
    }

    public boolean cancelChanneling() {
        return isChanneling() &&
                stopChanneling(channelingContext, false);
    }

    public boolean interruptChanneling() {
        return isChanneling() &&
                stopChanneling(channelingContext, true);
    }

    public boolean dispatchChannelingEvent(Function<ChannelingContext, ChannelingEvent> mapper) {
        return !isChanneling() ||
                channelingContext.getChanneling().getEventDispatcher().dispatch(mapper.apply(channelingContext));
    }

    @ApiStatus.Internal
    public void update() {
        if (isChanneling()) {
            updateChanneling(channelingContext);
        }
    }

    private void beginChanneling(ChannelingContext channelingContext) {
        this.channelingContext = channelingContext;
        channelingContext.getChanneling().getAttributeModifiers().addModifiers(channelingContext.getSource().getAttributes());
        channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingStartEvent(channelingContext));
    }

    private void endChanneling(ChannelingContext channelingContext) {
        channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingEndEvent(channelingContext));
        channelingContext.getChanneling().getAttributeModifiers().removeModifiers(channelingContext.getSource().getAttributes());
        this.channelingContext = null;
    }

    private boolean stopChanneling(ChannelingContext channelingContext, boolean force) {
        ChannelingEvent event = force ?
                new ChannelingInterruptEvent(channelingContext) :
                new ChannelingCancelEvent(channelingContext);

        if (!channelingContext.getChanneling().getEventDispatcher().dispatch(event)) {
            return false;
        }

        endChanneling(channelingContext);
        return true;
    }

    private void updateChanneling(ChannelingContext channelingContext) {
        channelingContext.setDuration(channelingContext.getDuration() - 1);
        if (channelingContext.getDuration() > 0) {
            channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingTickEvent(channelingContext));
        } else {
            channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingCompleteEvent(channelingContext));
            channelingContext.getChanneling().getAction().execute(channelingContext);
            endChanneling(channelingContext);
        }
    }

    private ChannelingContext channelingContext(Channeling channeling) {
        return new ChannelingContext(channeling, entitySupplier.get(), AttributeBag.create(), channeling.getDuration());
    }
}
