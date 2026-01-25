package net.collectively.v1.cardinal_components_api._internal;

import net.collectively.v1.Geode;
import net.collectively.v1.cardinal_components_api.player.PlayerCameraShakeComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class GeodeEntityComponentIndex implements EntityComponentInitializer {
    public static final ComponentKey<PlayerCameraShakeComponent> PLAYER_CAMERA_SHAKE = ComponentRegistry.getOrCreate(
            Geode.internalId("player_camera_shake"),
            PlayerCameraShakeComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, PLAYER_CAMERA_SHAKE, PlayerCameraShakeComponent::new);
    }
}
