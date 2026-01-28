package dev.rosenoire.testmod;

import net.collectively.v2.datagen.GeodeDataGeneration;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TestingModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(Provider::new);
    }

    static class Provider extends GeodeDataGeneration {
        public Provider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate() {
            addEnchantment(TestingMod.SHARPNESS_COPYCAT)
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
}
