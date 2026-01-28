package net.collectively.geode.datagen;

import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class DataGenRunnable<T> {
    protected final T value;

    public DataGenRunnable(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    protected abstract void run(RegistryWrapper.@NotNull WrapperLookup registries, GeodeDataGeneration.DataGen dataGen);
}
