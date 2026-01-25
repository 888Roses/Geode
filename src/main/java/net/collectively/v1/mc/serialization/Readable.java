package net.collectively.v1.mc.serialization;

import net.minecraft.storage.ReadView;

public interface Readable {
    void read(ReadView view);
}
