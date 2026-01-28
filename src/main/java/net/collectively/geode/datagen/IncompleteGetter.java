package net.collectively.geode.datagen;

import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IncompleteGetter<T> {
    T complete(RegistryWrapper.@NotNull WrapperLookup registries);
}
