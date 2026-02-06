package net.collectively.geode.registration;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record GeodeEnchantment(RegistryKey<Enchantment> registryKey) {
    public Identifier identifier() {
        return registryKey.getValue();
    }

    public @Nullable RegistryEntry<Enchantment> getRegistryEntry(ServerWorld serverWorld) {
        return serverWorld.getRegistryManager()
                .getOptional(RegistryKeys.ENCHANTMENT)
                .flatMap(x -> x.getEntry(registryKey.getValue()))
                .orElse(null);
    }

    public int getLevel(ServerWorld serverWorld, ItemStack itemStack) {
        RegistryEntry<Enchantment> registryEntry = getRegistryEntry(serverWorld);
        return EnchantmentHelper.getLevel(registryEntry, itemStack);
    }

    public boolean hasEnchantment(ServerWorld serverWorld, ItemStack itemStack) {
        return getLevel(serverWorld, itemStack) > 0;
    }
}