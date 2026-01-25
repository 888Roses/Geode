package net.collectively.v2.mixin.client.options;

import net.collectively.v2.registration.ClientCustomRegistries;
import net.collectively.v2.registration.Option;
import net.collectively.v2.registration.OptionScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinOptionsScreen.class)
public class SkinOptionsScreenMixin {
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void serverWipe$getOptions(CallbackInfo ci) {
        ((SkinOptionsScreen) (Object) this).body.addAll(
                ClientCustomRegistries.OPTIONS
                        .stream()
                        .filter(x -> x.screen() == OptionScreen.SKIN)
                        .map(Option::option)
                        .toArray(SimpleOption<?>[]::new)
        );
    }
}
