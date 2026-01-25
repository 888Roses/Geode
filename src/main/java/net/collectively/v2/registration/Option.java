package net.collectively.v2.registration;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.Identifier;

public record Option<T>(String identifier, SimpleOption<T> option, OptionScreen screen) {
    public static String getTranslationKey(Identifier identifier) {
        return "option." + identifier.getNamespace() + "." + identifier;
    }
}
