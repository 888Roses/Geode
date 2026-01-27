package net.collectively.v2.datagen;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record IncompleteEnchantmentEntry(Identifier identifier, GeodeDatagen.IncompleteEnchantment incompleteEnchantment) {
    public EnchantmentEntry complete(RegistryWrapper.@NotNull WrapperLookup lookup) {
        return new EnchantmentEntry(identifier(), incompleteEnchantment.complete(lookup));
    }
}
