package net.collectively.v2.datagen;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;

public record EnchantmentEntry(Identifier identifier, Enchantment value) {
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof EnchantmentEntry entry) {
            return entry.identifier().equals(identifier());
        }

        return false;
    }
}
