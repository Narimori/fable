package org.narimori.fable.channeling.event;

import org.narimori.fable.channeling.ChannelingContext;
import org.narimori.fable.core.event.Event;

public interface ChannelingEvent extends Event {
    ChannelingContext getContext();
}
