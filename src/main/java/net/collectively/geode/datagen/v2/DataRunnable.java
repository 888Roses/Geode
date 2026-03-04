package net.collectively.geode.datagen.v2;

import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

/// Represents a geode Data Generator entry that can be used to generate data for a certain type.
public abstract class DataRunnable<T> {
    private final T value;

    public DataRunnable(T value) {
        this.value = value;
    }

    /// The value contained in this runnable.
    public T value() {
        return value;
    }

    /// Enqueues the data generation into the given [GeodeDataGenerator], or generates the custom datagen if necessary.
    /// @param registries The registries future.
    /// @param generator Generator actually generating the data generation.
    protected abstract void collect(RegistryWrapper.@NotNull WrapperLookup registries, GeodeDataGenerator generator);
}