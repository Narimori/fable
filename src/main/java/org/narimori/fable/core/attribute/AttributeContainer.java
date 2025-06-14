package org.narimori.fable.core.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class AttributeContainer {
    private final @NotNull Map<@NotNull String, @NotNull Object> attributes = new HashMap<>();

    private AttributeContainer() {
    }

    public static @NotNull AttributeContainer create() {
        return new AttributeContainer();
    }

    public boolean containsKey(@NotNull String key) {
        Objects.requireNonNull(key, "key must not be null");
        return attributes.containsKey(key);
    }

    public boolean containsValue(@NotNull Object value) {
        Objects.requireNonNull(value, "value must not be null");
        return attributes.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(@NotNull String key) {
        Objects.requireNonNull(key, "key must not be null");
        return (T) attributes.get(key);
    }

    public <T> @NotNull T getOrThrow(@NotNull String key) {
        Objects.requireNonNull(key, "key must not be null");
        T value = get(key);
        if (value == null) {
            throw new NoSuchElementException();
        }

        return value;
    }

    public <T> @NotNull T getOrDefault(
            @NotNull String key,
            @NotNull T defaultValue
    ) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");
        T value = get(key);
        return value == null ? defaultValue : value;
    }

    public void set(
            @NotNull String key,
            @NotNull Object value
    ) {
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");
        attributes.put(key, value);
    }

    public void remove(@NotNull String key) {
        Objects.requireNonNull(key, "key must not be null");
        attributes.remove(key);
    }
}
