package org.narimori.fable.core.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
