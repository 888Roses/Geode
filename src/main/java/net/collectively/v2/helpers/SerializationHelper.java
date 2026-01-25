package net.collectively.v2.helpers;

import net.collectively.v2.types.double3;
import net.minecraft.storage.ReadView;

/// Collection of utilities regarding serialization and [codecs][com.mojang.serialization.Codec].
@SuppressWarnings("unused")
public interface SerializationHelper {
    /// Reads the value with the `String` name in the given [read view][ReadView], or returns the [fallback][double3] if no
    /// value with the given name could be found in the view.
    ///
    /// @param readView The view to read in.
    /// @param name The name of the saved value.
    /// @param fallback The value to return in case no value with the given name could be found in the view.
    /// @return The read value or the fallback if it couldn't be found.
    static double3 readDouble3(ReadView readView, String name, double3 fallback) {
        return readView.read(name, double3.CODEC).orElse(fallback);
    }
}
