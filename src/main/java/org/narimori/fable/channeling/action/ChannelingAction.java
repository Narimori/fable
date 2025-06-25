package org.narimori.fable.channeling.action;

import org.jetbrains.annotations.ApiStatus;
import org.narimori.fable.channeling.ChannelingContext;

import java.util.Objects;

@FunctionalInterface
public interface ChannelingAction {
    static ChannelingAction noOp() {
        return context -> {};
    }

    void execute(ChannelingContext context);

    @ApiStatus.NonExtendable
    default ChannelingAction andThen(ChannelingAction after) {
        Objects.requireNonNull(after);
        return (context) -> {
            execute(context);
            after.execute(context);
        };
    }
}
