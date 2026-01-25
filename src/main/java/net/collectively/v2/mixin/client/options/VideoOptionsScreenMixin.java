package net.collectively.v2.mixin.client.options;

import net.collectively.v2.registration.ClientCustomRegistries;
import net.collectively.v2.registration.Option;
import net.collectively.v2.registration.OptionScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void serverWipe$getOptions(CallbackInfo ci) {
        ((VideoOptionsScreen) (Object) this).body.addAll(
                ClientCustomRegistries.OPTIONS
                        .stream()
                        .filter(x -> x.screen() == OptionScreen.VIDEO)
                        .map(Option::option)
                        .toArray(SimpleOption<?>[]::new)
        );
    }
}
