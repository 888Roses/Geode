package dev.rosenoire.testmod;

import net.collectively.v2.datagen.GeodeDataGeneration;
import net.collectively.v2.datagen.GeodeDatagen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TestingModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(Datagen2::new);
        // pack.addProvider(ModLangGenerator::new);
        // pack.addProvider(Datagen::new);
    }

    static class Datagen2 extends GeodeDataGeneration {
        public Datagen2(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void generate() {
            addEnchantment(TestingMod.SHARPNESS_COPYCAT)
                    .enchantment(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                    .primaryItems(ItemTags.MELEE_WEAPON_ENCHANTABLE)
                    .weight(10)
                    .maxLevel(5)
                    .minCost(1, 11)
                    .maxCost(21, 11)
                    .anvilCost(1)
                    .addSlot(AttributeModifierSlot.MAINHAND)
                    .build()
                    .autoTranslate();
        }
    }

    static class Datagen extends GeodeDatagen {
        public Datagen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void generate() {
            addEnchantment(TestingMod.SHARPNESS_COPYCAT, enchantment(
                    enchantmentDefinition(itemTag(ItemTags.SHARP_WEAPON_ENCHANTABLE))
                            .primaryItems(itemTag(ItemTags.MELEE_WEAPON_ENCHANTABLE))
                            .weight(10)
                            .maxLevel(5)
                            .minCost(1, 11)
                            .maxCost(21, 11)
                            .anvilCost(1)
                            .addSlot(AttributeModifierSlot.MAINHAND)
                            .build())
                    .exclusiveSet(enchantmentTag(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                    .addEffect(EnchantmentEffectComponentTypes.DAMAGE, new AddEnchantmentEffect(
                            EnchantmentLevelBasedValue.linear(1.0F, 0.5F)
                    ))
            );
        }
    }
}
