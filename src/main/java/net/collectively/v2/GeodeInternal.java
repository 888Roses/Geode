package net.collectively.v2;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// Internal class for Geode containing several shared utilities for internal behavior.
/// Should not be used outside the geode package.
@SuppressWarnings("unused")
@ApiStatus.Internal
public class GeodeInternal {
    public static final String MOD_ID = "geode";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /// Creates a new [identifier][Identifier] using the given `String` path of the identifier.
    /// @param path The second part, or real identifier of the created [Identifier].
    /// @return A new identifier created using [Identifier#of(String, String)] and the [#MOD_ID] as the namespace and the provided `String` as the path.
    public static Identifier internalId(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
