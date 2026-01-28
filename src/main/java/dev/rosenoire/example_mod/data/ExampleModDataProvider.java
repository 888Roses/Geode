package dev.rosenoire.example_mod.data;

import dev.rosenoire.example_mod.common.index.ModEnchantments;
import dev.rosenoire.example_mod.common.index.ModItemGroups;
import dev.rosenoire.example_mod.common.index.ModItems;
import net.collectively.geode.datagen.GeodeDataGeneration;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ExampleModDataProvider extends GeodeDataGeneration {
    public ExampleModDataProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    protected void generate() {
        addItemGroup(ModItemGroups.EXAMPLE_MOD_GROUP)
                .autoTranslate();

        addItem(ModItems.ITEM_IDENTIFIER)
                .autoTranslate();

        addEnchantment(ModEnchantments.SHARPNESS_COPYCAT)
                .autoTranslate()
                .enchantment(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .primaryItems(ItemTags.MELEE_WEAPON_ENCHANTABLE)
                .weight(10)
                .maxLevel(5)
                .minCost(1, 11)
                .maxCost(21, 11)
                .anvilCost(1)
                .addSlot(AttributeModifierSlot.MAINHAND)
                .build()
                .tag(EnchantmentTags.TRADEABLE)
                .tag(EnchantmentTags.ON_RANDOM_LOOT);
    }
}
