package net.collectively.geode.registration;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public record GeodeEnchantment(RegistryKey<Enchantment> registryKey) {
    public Identifier identifier() {
        return registryKey.getValue();
    }
}