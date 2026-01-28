package net.collectively.geode.mixin.client.options;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.collectively.geode.registration.ClientCustomRegistries;
import net.collectively.geode.registration.Option;
import net.collectively.geode.registration.OptionScreen;
import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
    @ModifyReturnValue(method = "getOptions", at = @At("RETURN"))
    private static SimpleOption<?>[] serverWipe$getOptions(SimpleOption<?>[] original) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(Arrays.asList(original));

        list.addAll(ClientCustomRegistries.OPTIONS
                .stream()
                .filter(x -> x.screen() == OptionScreen.CHAT)
                .map(Option::option)
                .toList()
        );

        list.removeIf(x ->
                x.text.getContent() instanceof TranslatableTextContent translatableTextContent &&
                        ClientCustomRegistries.REMOVED_OPTIONS.contains(translatableTextContent.getKey())
        );

        return list.toArray(SimpleOption<?>[]::new);
    }
}
