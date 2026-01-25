package net.collectively.v2;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Supplier;

/// Represents a created [group][ItemGroup].
///
/// @param registryKey The [key][RegistryKey] of that group, containing its [identifier][Identifier].
/// @param itemGroup The created group.
/// @param content A supplier providing every item to be added to the group when registering it.
///
/// @see Registerer#registerGroup(Identifier, ItemGroup.Builder, Item...)
public record GeodeGroup(RegistryKey<ItemGroup> registryKey, ItemGroup itemGroup, Supplier<Item[]> content) {
    /// Registers this group in the [Registries#ITEM_GROUP] registry.
    /// **Warning**: this is an **internal** method and should not be called if you don't know what you're doing!
    public void register() {
        Registry.register(Registries.ITEM_GROUP, registryKey, itemGroup);
        ItemGroupEvents.modifyEntriesEvent(registryKey).register(group ->
                group.addAll(Arrays.stream(content.get()).map(ItemStack::new).toList())
        );
    }
}