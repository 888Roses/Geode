package dev.rosenoire.testmod;

import net.collectively.v2.datagen.GeodeEnchantmentGenerator;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModEnchantmentGenerator extends GeodeEnchantmentGenerator {
    public ModEnchantmentGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, List.of(
                new EnchantmentGenerator() {
                    @Override
                    public void accept(RegistryWrapper.WrapperLookup registries, Consumer<EnchantmentEntry> exporter) {
                        RegistryWrapper.Impl<Item> itemRegistry = registries.getOrThrow(RegistryKeys.ITEM);
                        exporter.accept(new EnchantmentEntry(TestingMod.MEANING.identifier(), Enchantment
                                .builder(Enchantment.definition(
                                        itemRegistry.getOrThrow(ItemTags.SWORDS),
                                        15,
                                        1,
                                        new Enchantment.Cost(1, 1),
                                        new Enchantment.Cost(1, 1), 15,
                                        AttributeModifierSlot.MAINHAND
                                ))
                                .build(TestingMod.MEANING.identifier())
                        ));
                    }
                }
        ));
    }
}
