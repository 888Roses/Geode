package net.collectively.geode.cardinal_components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public abstract class SyncedPlayerComponent implements Component, AutoSyncedComponent {
    private final PlayerEntity player;

    public SyncedPlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    // region ---- RECORD ACCESSOR ----

    public PlayerEntity player() {
        return player;
    }

    public World world() {
        return player.getEntityWorld();
    }

    // endregion

    // region -------- SYNCING --------

    protected abstract ComponentKey<? extends SyncedPlayerComponent> getComponentKey();

    public final void sync(PlayerEntity target) {
        getComponentKey().sync(target);
    }

    public final void sync() {
        sync(player);
    }

    // endregion
}
