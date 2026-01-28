package dev.rosenoire.example_mod.common;

import dev.rosenoire.example_mod.common.index.ModCommands;
import dev.rosenoire.example_mod.common.index.ModEnchantments;
import dev.rosenoire.example_mod.common.index.ModItemGroups;
import dev.rosenoire.example_mod.common.index.ModItems;
import net.collectively.geode.Geode;
import net.fabricmc.api.ModInitializer;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "testing_mod";
    public static final Geode geode = Geode.create(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerAll();
        ModItemGroups.registerAll();
        ModEnchantments.registerAll();
        ModCommands.registerAll();

        // Always called last.
        geode.register();
    }
}
