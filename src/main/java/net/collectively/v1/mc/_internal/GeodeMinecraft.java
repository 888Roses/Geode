package net.collectively.v1.mc._internal;

import net.collectively.v1.mc._internal.index.GeodeMinecraftPayloadRegistry;

public interface GeodeMinecraft {
    static void initialize() {
        GeodeMinecraftPayloadRegistry.registerAll();
    }
}
