package dev.rosenoire.testmod;

import net.collectively.v2.GeodeClient;
import net.collectively.v2.registration.Option;
import net.collectively.v2.registration.OptionScreen;
import net.fabricmc.api.ClientModInitializer;

public class TestingModClient implements ClientModInitializer {
    public static final GeodeClient geode = GeodeClient.create(TestingMod.MOD_ID);

    public static final Option<Boolean> IS_AWESOME = geode.registerBooleanOption("is_awesome", OptionScreen.MOUSE, false);

    @Override
    public void onInitializeClient() {
        geode.registerRemovedOption("options.sensitivity");

        geode.register();
    }
}
