package net.collectively.geode.cardinal_components.index;

import net.collectively.geode.cardinal_components.SyncedPlayerComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentFactory;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;

public record EntityComponentFactoryRegistryImpl(EntityComponentFactoryRegistry registry) {
    public <T extends SyncedPlayerComponent> void registerSyncedPlayer(ComponentKey<T> key, ComponentFactory<PlayerEntity, T> factory) {
        registry.registerFor(PlayerEntity.class, key, factory);
    }
}