package dev.rosenoire.example_mod.common.index;

import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;

import static dev.rosenoire.example_mod.common.ExampleMod.geode;

public interface ModItems {
    Item ITEM_STRING = geode.registerItem("item_string");
    Item ITEM_IDENTIFIER = geode.registerItem(geode.id("item_identifier"));
    Item ITEM_STRING_SETTINGS = geode.registerItem("item_string_settings", new Item.Settings().maxCount(2));
    Item ITEM_IDENTIFIER_SETTINGS = geode.registerItem(geode.id("item_identifier_settings"), new Item.Settings().maxCount(2));
    Item ITEM_STRING_ITEM_SETTINGS = geode.registerItem("item_string_item_settings", MaceItem::new, new Item.Settings().maxCount(2));
    Item ITEM_IDENTIFIER_ITEM_SETTINGS = geode.registerItem(geode.id("item_identifier_item_settings"), MaceItem::new, new Item.Settings().maxCount(2));

    static void registerAll() {
    }
}
