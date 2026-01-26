package dev.rosenoire.testmod;

import net.collectively.v2.Geode;
import net.collectively.v2.registration.GeodeGroup;
import net.collectively.v2.registration.ItemGroupBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.util.Identifier;

public class TestingMod implements ModInitializer {
    public static final String MOD_ID = "testing_mod";
    public static final Geode geode = Geode.create(MOD_ID);

    public static final Item ITEM_STRING = geode.registerItem("item_string");
    public static final Item ITEM_IDENTIFIER = geode.registerItem(Identifier.of(MOD_ID, "item_identifier"));
    public static final Item ITEM_STRING_SETTINGS = geode.registerItem("item_string_settings", new Item.Settings().maxCount(2));
    public static final Item ITEM_IDENTIFIER_SETTINGS = geode.registerItem(Identifier.of(MOD_ID, "item_identifier_settings"), new Item.Settings().maxCount(2));
    public static final Item ITEM_STRING_ITEM_SETTINGS = geode.registerItem("item_string_item_settings", MaceItem::new, new Item.Settings().maxCount(2));
    public static final Item ITEM_IDENTIFIER_ITEM_SETTINGS = geode.registerItem(Identifier.of(MOD_ID, "item_identifier_item_settings"), MaceItem::new, new Item.Settings().maxCount(2));

    public static final GeodeGroup EXAMPLE_MOD_GROUP = geode.registerGroup(
            "example_mod",
            ItemGroupBuilder.of().withIcon(() -> new ItemStack(Items.GOLDEN_HELMET)),
            ITEM_STRING,
            ITEM_IDENTIFIER,
            ITEM_STRING_SETTINGS,
            ITEM_IDENTIFIER_SETTINGS,
            ITEM_STRING_ITEM_SETTINGS,
            ITEM_IDENTIFIER_ITEM_SETTINGS
    );

    @Override
    public void onInitialize() {
        TestingModCommands.register();
        geode.register();
    }
}
