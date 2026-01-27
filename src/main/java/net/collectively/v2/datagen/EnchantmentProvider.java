package net.collectively.v2.datagen;

import net.collectively.v2.registration.GeodeEnchantment;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class EnchantmentProvider implements EnchantmentGenerator {
    private final List<EnchantmentEntry> entries = new ArrayList<>();
    private RegistryWrapper.WrapperLookup registries;

    @Override
    public final void accept(RegistryWrapper.WrapperLookup registries, Consumer<EnchantmentEntry> exporter) {
        this.registries = registries;
        generate();

        for (EnchantmentEntry entry : entries) {
            exporter.accept(entry);
        }
    }

    protected abstract void generate();

    public RegistryWrapper.WrapperLookup getRegistries() {
        return registries;
    }

    protected final <T> RegistryWrapper.Impl<T> getRegistry(RegistryKey<? extends Registry<T>> registryKey) {
        return registries.getOrThrow(registryKey);
    }
    protected final RegistryEntryList.Named<Item> itemTag(TagKey<Item> tag) {return getRegistry(RegistryKeys.ITEM).getOrThrow(tag);}
    protected final RegistryEntryList.Named<DamageType> damageTag(TagKey<DamageType> tag) {return getRegistry(RegistryKeys.DAMAGE_TYPE).getOrThrow(tag);}
    protected final RegistryEntryList.Named<Enchantment> enchantmentTag(TagKey<Enchantment> tag) {return getRegistry(RegistryKeys.ENCHANTMENT).getOrThrow(tag);}
    protected final RegistryEntryList.Named<Block> blockTag(TagKey<Block> tag) {return getRegistry(RegistryKeys.BLOCK).getOrThrow(tag);}
    protected final RegistryEntryList.Named<EntityType<?>> entityTypeTag(TagKey<EntityType<?>> tag) {return getRegistry(RegistryKeys.ENTITY_TYPE).getOrThrow(tag);}

    protected final Enchantment.Cost cost(int base, int increment) {return new Enchantment.Cost(base, increment);}

    protected final void add(Identifier identifier, Enchantment enchantment) {
        entries.add(new EnchantmentEntry(identifier, enchantment));
    }
    protected final void add(GeodeEnchantment geodeEnchantment, Enchantment enchantment) {
        add(geodeEnchantment.identifier(), enchantment);
    }
    protected final void add(Identifier identifier, Enchantment.Builder enchantmentBuilder) {
        add(identifier, enchantmentBuilder.build(identifier));
    }
    protected final void add(GeodeEnchantment geodeEnchantment, Enchantment.Builder enchantmentBuilder) {
        add(geodeEnchantment.identifier(), enchantmentBuilder);
    }
    protected final void add(Identifier identifier, Enchantment.Definition enchantmentDefinition) {
        add(identifier, Enchantment.builder(enchantmentDefinition));
    }
    protected final void add(GeodeEnchantment geodeEnchantment, Enchantment.Definition enchantmentDefinition) {
        add(geodeEnchantment, Enchantment.builder(enchantmentDefinition));
    }
}
