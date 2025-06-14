package org.narimori.fable.channeling.manager;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narimori.fable.channeling.Channeling;
import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.channeling.event.ChannelingEvent;
import org.narimori.fable.channeling.event.lifecycle.*;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChannelingManager {
    private final @NotNull Supplier<@NotNull LivingEntity> entitySupplier;

    private @Nullable ChannelingContext channelingContext = null;

    @ApiStatus.Internal
    public ChannelingManager(@NotNull Supplier<@NotNull LivingEntity> entitySupplier) {
        this.entitySupplier = Objects.requireNonNull(entitySupplier, "entitySupplier must not be null");
    }

    public boolean isChanneling() {
        return channelingContext != null;
    }

    public boolean startChanneling(@NotNull Channeling channeling) {
        Objects.requireNonNull(channeling, "channeling must not be null");
        if (channelingContext != null) {
            return false;
        }

        beginChanneling(channelingContext(channeling));
        return true;
    }

    public boolean cancelChanneling() {
        if (channelingContext == null) {
            return false;
        }

        return stopChanneling(channelingContext, true);
    }

    public boolean interruptChanneling() {
        if (channelingContext == null) {
            return false;
        }

        return stopChanneling(channelingContext, false);
    }

    public <T extends ChannelingEvent> void dispatchChannelingEvent(
            @NotNull Function<@NotNull ChannelingContext, @NotNull T> mapper,
            @NotNull Consumer<@NotNull T> afterAction
    ) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        Objects.requireNonNull(afterAction, "afterAction must not be null");
        if (channelingContext == null) {
            return;
        }

        T event = mapper.apply(channelingContext);
        channelingContext.getChanneling().getEventDispatcher().dispatch(event);
        afterAction.accept(event);
    }

    @ApiStatus.Internal
    public void afterTick() {
        if (channelingContext != null) {
            updateChanneling(channelingContext);
        }
    }

    private void beginChanneling(@NotNull ChannelingContext channelingContext) {
        this.channelingContext = channelingContext;

        Channeling channeling = channelingContext.getChanneling();
        channeling.getAttributeModifiers().addModifiers(entitySupplier.get().getAttributes());
        channeling.getEventDispatcher().dispatch(new ChannelingStartEvent(channelingContext));
    }

    private void endChanneling(@NotNull ChannelingContext channelingContext) {
        Channeling channeling = channelingContext.getChanneling();
        channeling.getEventDispatcher().dispatch(new ChannelingEndEvent(channelingContext));
        channeling.getAttributeModifiers().removeModifiers(entitySupplier.get().getAttributes());

        this.channelingContext = null;
    }

    private boolean stopChanneling(@NotNull ChannelingContext channelingContext, boolean shouldCancel) {
        if (shouldCancel) {
            ChannelingCancelEvent event = new ChannelingCancelEvent(channelingContext);
            channelingContext.getChanneling().getEventDispatcher().dispatch(event);

            if (event.isCancelled()) {
                return false;
            }
        } else {
            ChannelingInterruptEvent event = new ChannelingInterruptEvent(channelingContext);
            channelingContext.getChanneling().getEventDispatcher().dispatch(event);

            if (event.isCancelled()) {
                return false;
            }
        }

        endChanneling(channelingContext);
        return true;
    }

    private void updateChanneling(@NotNull ChannelingContext channelingContext) {
        channelingContext.setDuration(channelingContext.getDuration() - 1);
        if (channelingContext.getDuration() > 0) {
            channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingTickEvent(channelingContext));
        } else {
            channelingContext.getChanneling().getEventDispatcher().dispatch(new ChannelingCompleteEvent(channelingContext));
            endChanneling(channelingContext);
        }
    }

    private @NotNull ChannelingContext channelingContext(@NotNull Channeling channeling) {
        return new ChannelingContext(channeling, entitySupplier.get(), channeling.getDuration());
    }
}
