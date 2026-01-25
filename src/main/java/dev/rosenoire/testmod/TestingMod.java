package dev.rosenoire.testmod;

import net.collectively.v2.Geode;
import net.fabricmc.api.ModInitializer;

public class TestingMod implements ModInitializer {
    public static final String MOD_ID = "testing_mod";
    public static final Geode GEODE = Geode.create(MOD_ID);

    @Override
    public void onInitialize() {
    }
}
