package dev.rosenoire.example_mod.common.index;

import net.collectively.v2.registration.GeodeEnchantment;
import static dev.rosenoire.example_mod.common.ExampleMod.geode;

public interface ModEnchantments {
    GeodeEnchantment SHARPNESS_COPYCAT = geode.registerEnchantment("sharpness_copycat");

    static void registerAll() {}
}
