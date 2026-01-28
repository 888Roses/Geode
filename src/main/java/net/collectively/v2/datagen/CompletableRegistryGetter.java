package net.collectively.v2.datagen;

import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CompletableRegistryGetter<T> {
    T complete(RegistryWrapper.@NotNull WrapperLookup registries);
}
