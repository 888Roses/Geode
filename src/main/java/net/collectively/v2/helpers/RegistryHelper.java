package net.collectively.v2.helpers;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

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
    static <T> @Nullable Identifier getIdentifierOf(RegistryWrapper.@NonNull WrapperLookup registryLookup,
                                                    @NonNull RegistryKey<Registry<T>> registryKey, @NonNull T value) {
        Optional<RegistryEntry.Reference<Registry<T>>> optionalEntry = registryLookup.getOptionalEntry(registryKey);
        return optionalEntry.map(x -> x.value().getId(value)).orElse(null);
    }
}
