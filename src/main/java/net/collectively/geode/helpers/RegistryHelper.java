package net.collectively.geode.helpers;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/// Collection of utilities regarding the management of [registries][Registry] and alike.
@SuppressWarnings("unused")
public interface RegistryHelper {
    /// Returns the [identifier][Identifier] of the [given value][T] in its registry, represented by the [RegistryKey].
    /// The actual `Registry` is then retrieved from the [registry lookup][RegistryWrapper.WrapperLookup].
    /// If the given value couldn't be found in the registry, returns null.
    ///
    /// @param registryLookup Wrapper lookup to get the registry from.
    /// @param registryKey Registry key referencing the registry in which the given value is registered.
    /// @param value The value we want to get the identifier of.
    /// @param <T> The type of the value.
    /// @return The identifier of the given value as registered in the given registries, or null if it wasn't registered.
    /// @see #getIdentifierOf(World, RegistryKey, Object) 
    static <T> @Nullable Identifier getIdentifierOf(RegistryWrapper.@NonNull WrapperLookup registryLookup,
                                                    @NonNull RegistryKey<Registry<T>> registryKey, @NonNull T value) {
        // Black magic.
        return registryLookup
                .getOptional(registryKey)
                .flatMap(registry -> registry
                        .streamEntries()
                        .filter(entry -> entry.value().equals(value))
                        .findFirst()
                        .map(entry -> entry.registryKey().getValue())
                )
                .orElse(null);
    }

    /// Returns the [identifier][Identifier] of the [given value][T] in its registry, represented by the [RegistryKey].
    /// The actual `Registry` is then retrieved from the [registry lookup][RegistryWrapper.WrapperLookup] contained in the given [world][World].
    /// If the given value couldn't be found in the registry, returns null.
    ///
    /// @param world The world in which the value is, from which we retrieve the registry lookup necessary to find our identifier.
    /// @param registryKey Registry key referencing the registry in which the given value is registered.
    /// @param value The value we want to get the identifier of.
    /// @param <T> The type of the value.
    /// @return The identifier of the given value as registered in the given registries, or null if it wasn't registered.
    /// @see #getIdentifierOf(RegistryWrapper.WrapperLookup, RegistryKey, Object) 
    static <T> @Nullable Identifier getIdentifierOf(World world, @NonNull RegistryKey<Registry<T>> registryKey, @NonNull T value) {
        return getIdentifierOf(world.getRegistryManager(), registryKey, value);
    }
}
