package net.collectively.v2.mixin.client.options;

import net.collectively.v2.registration.ClientCustomRegistries;
import net.collectively.v2.registration.Option;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;acceptProfiledOptions(Lnet/minecraft/client/option/GameOptions$OptionVisitor;)V"))
    private void accept(GameOptions.Visitor visitor, CallbackInfo ci) {
        for (Option<?> option : ClientCustomRegistries.OPTIONS) {
            visitor.accept(option.identifier(), option.option());
        }
    }
}