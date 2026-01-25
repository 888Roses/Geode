package net.collectively.v1.mc.serialization;

import net.minecraft.storage.WriteView;

public interface Writable {
    void write(WriteView view);
}
