package dev.rosenoire.example_mod.client;

import dev.rosenoire.example_mod.common.ExampleMod;
import net.collectively.v2.GeodeClient;
import net.collectively.v2.registration.Option;
import net.collectively.v2.registration.OptionScreen;
import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
    public static final GeodeClient geode = GeodeClient.create(ExampleMod.MOD_ID);

    public static final Option<Boolean> IS_AWESOME = geode.registerBooleanOption("is_awesome", OptionScreen.MOUSE, false);

    @Override
    public void onInitializeClient() {
        geode.registerRemovedOption("options.sensitivity");

        geode.register();
    }
}
