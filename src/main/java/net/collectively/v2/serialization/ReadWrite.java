package net.collectively.v2.serialization;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

/// Offers a concise way to read and save data.
@SuppressWarnings("unused")
public interface ReadWrite {
    /// Provides a [write view][WriteView] in which data can be saved.
    void write(WriteView writeView);

    /// Provides a [read view][ReadView] from which data can be read.
    void read(ReadView readView);
}
