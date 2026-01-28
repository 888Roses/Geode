package dev.rosenoire.example_mod.common.index;

import net.collectively.v2.registration.GeodeGroup;
import net.collectively.v2.registration.ItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static dev.rosenoire.example_mod.common.ExampleMod.geode;
import static dev.rosenoire.example_mod.common.index.ModItems.*;

public interface ModItemGroups {
    GeodeGroup EXAMPLE_MOD_GROUP = geode.registerGroup(
            "example_mod",
            ItemGroupBuilder.of().withIcon(() -> new ItemStack(Items.GOLDEN_HELMET)),
            ITEM_STRING,
            ITEM_IDENTIFIER,
            ITEM_STRING_SETTINGS,
            ITEM_IDENTIFIER_SETTINGS,
            ITEM_STRING_ITEM_SETTINGS,
            ITEM_IDENTIFIER_ITEM_SETTINGS
    );

    static void registerAll() {}
}
