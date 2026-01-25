package net.collectively.v1.cardinal_components_api.player;

import net.collectively.v1.cardinal_components_api._internal.GeodeEntityComponentIndex;
import net.minecraft.entity.player.PlayerEntity;

public interface Shake {
    static void shake(PlayerEntity player, ShakeSettings settings) {
        player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE).shake(settings);
    }
}
