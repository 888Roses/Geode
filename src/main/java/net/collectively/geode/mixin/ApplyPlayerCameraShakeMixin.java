package net.collectively.geode.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.collectively.geode.cardinal_components_api._internal.GeodeEntityComponentIndex;
import net.collectively.geode.cardinal_components_api.player.PlayerCameraShakeComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class ApplyPlayerCameraShakeMixin {
    @ModifyReturnValue(method = "getRotation", at = @At("RETURN"))
    private Quaternionf update$characterEngine(Quaternionf original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null) {
            return original;
        }

        PlayerCameraShakeComponent component = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);

        if (component.isShaking()) {
            float correctedPitch = (float) component.getCurrentShakeVector().x();
            float correctedYaw = (float) component.getCurrentShakeVector().y();
            return original.rotateX(correctedPitch).rotateY(correctedYaw);
        }

        return original;
    }
}
