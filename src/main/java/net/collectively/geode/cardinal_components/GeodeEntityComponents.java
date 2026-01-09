package net.collectively.geode.cardinal_components;

import net.collectively.geode.Geode;
import net.collectively.geode.cardinal_components.index.EntityComponentFactoryRegistryImpl;
import net.collectively.geode.cardinal_components.index.EntityComponentIndex;
import net.collectively.geode.player.delays.PlayerDelaysComponent;
import org.jetbrains.annotations.ApiStatus;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;

@ApiStatus.Internal
@SuppressWarnings("SameParameterValue")
public class GeodeEntityComponents extends EntityComponentIndex {
    public static final ComponentKey<PlayerDelaysComponent> PLAYER_DELAYS = keyOfInternal(
            "player_delays", PlayerDelaysComponent.class
    );

    @Override
    protected void register(EntityComponentFactoryRegistryImpl registries) {
        registries.registerSyncedPlayer(PLAYER_DELAYS, PlayerDelaysComponent::new);
    }

    private static <C extends Component> ComponentKey<C> keyOfInternal(String identifier, Class<C> clazz) {
        return keyOf(Geode.geodeId(identifier), clazz);
    }
}