package dev.rosenoire.testmod;

import net.collectively.v2.datagen.EnchantmentProvider;
import net.collectively.v2.datagen.GeodeEnchantmentGenerator;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.data.DataOutput;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModEnchantmentGenerator extends GeodeEnchantmentGenerator {
    public ModEnchantmentGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture, List.of(
                new EnchantmentProvider() {
                    @Override
                    protected void generate() {
                        add(TestingMod.MEANING,
                                Enchantment
                                        .builder(Enchantment.definition(
                                                itemTag(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                                itemTag(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                                                10,
                                                5,
                                                Enchantment.leveledCost(1, 11),
                                                Enchantment.leveledCost(21, 11),
                                                1,
                                                AttributeModifierSlot.MAINHAND
                                        ))
                                        .exclusiveSet(enchantmentTag(EnchantmentTags.DAMAGE_EXCLUSIVE_SET))
                                        .addEffect(
                                                EnchantmentEffectComponentTypes.DAMAGE,
                                                new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1.0F, 0.5F))
                                        )
                        );
                    }
                }
        ));
    }
}
