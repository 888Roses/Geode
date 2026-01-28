package net.collectively.geode.datagen;

import net.minecraft.registry.RegistryWrapper;

import java.util.function.Consumer;

public interface EnchantmentGenerator {
    void accept(RegistryWrapper.WrapperLookup registries, Consumer<EnchantmentEntry> exporter);
}