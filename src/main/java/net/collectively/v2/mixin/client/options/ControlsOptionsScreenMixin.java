package net.collectively.v2.mixin.client.options;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.collectively.v2.registration.ClientCustomRegistries;
import net.collectively.v2.registration.Option;
import net.collectively.v2.registration.OptionScreen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(ControlsOptionsScreen.class)
public class ControlsOptionsScreenMixin {
    @ModifyReturnValue(method = "getOptions", at = @At("RETURN"))
    private static SimpleOption<?>[] serverWipe$getOptions(SimpleOption<?>[] original) {
        ArrayList<SimpleOption<?>> list = new ArrayList<>(Arrays.asList(original));

        list.addAll(ClientCustomRegistries.OPTIONS
                .stream()
                .filter(x -> x.screen() == OptionScreen.CONTROLS)
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
