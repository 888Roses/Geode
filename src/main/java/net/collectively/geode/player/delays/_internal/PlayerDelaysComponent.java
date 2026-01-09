package net.collectively.geode.player.delays._internal;

import net.collectively.geode.cardinal_components.GeodeEntityComponents;
import net.collectively.geode.cardinal_components.SyncedPlayerComponent;
import net.collectively.geode.player.delays.DelayedAction;
import net.collectively.geode.player.delays.PlayerDelays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.ApiStatus;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

/// Internal. Do not use. Use {@link PlayerDelays} for an implementation.
@ApiStatus.Internal
public class PlayerDelaysComponent extends SyncedPlayerComponent implements ServerTickingComponent {
    /// Contains every delayed action to run.
    private final List<DelayedAction> delayedActions = new ArrayList<>();

    // region -------- INTERNAL --------

    public PlayerDelaysComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    protected ComponentKey<? extends SyncedPlayerComponent> getComponentKey() {
        return GeodeEntityComponents.PLAYER_DELAYS;
    }

    // endregion

    // region --------- SYNCED ---------

    @Override
    public void readData(ReadView readView) {
    }

    @Override
    public void writeData(WriteView writeView) {
    }

    // endregion

    // region --------- ACCESS ---------

    /// Retrieves the delay component attached to the given `PlayerEntity`, or null if none is attached (which should never
    /// happen).
    public static PlayerDelaysComponent get(PlayerEntity player) {
        return player.getComponent(GeodeEntityComponents.PLAYER_DELAYS);
    }

    /// Enqueues a new action to the {@link #delayedActions} list.
    public final void enqueue(DelayedAction action) {
        if (world().isClient()) {
            return;
        }

        delayedActions.add(action);
    }

    // endregion

    /// Tries to execute any valid delayed action every tick, and removes them from the {@link #delayedActions} list.
    @Override
    public void serverTick() {
        if (delayedActions.isEmpty()) {
            return;
        }

        long currentTime = world().getTime();
        // Cannot remove items from a collection being looped on, so we first make a
        // copy of the loop to loop on, and then we remove items from the original.
        List<DelayedAction> loopOnDelayedActions = List.copyOf(delayedActions);
        for (DelayedAction action : loopOnDelayedActions) {
            if (action.time() <= currentTime) {
                action.runnable().run();
                delayedActions.remove(action);
            }
        }
    }
}