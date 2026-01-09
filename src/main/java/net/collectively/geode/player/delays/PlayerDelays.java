package net.collectively.geode.player.delays;

import net.collectively.geode.player.delays._internal.PlayerDelaysComponent;
import net.minecraft.entity.player.PlayerEntity;

/// # PlayerDelays
///
/// Offers a way of enqueuing actions to be run after a given tick delay on the server side. Example use:
///
/// ```java
/// public class PlayerAbilityManager {
///     public static void useFireballAbility(PlayerEntity player, int count) {
///         for (int i = 0; i < count; i++) {
///             // Throws a fireball every 10 ticks.
///             long delay = i * 10;
///             // Will execute the method `spawnFireball` after the given tick delay.
///             PlayerDelays.enqueue(player, delay, () -> spawnFireball(player));
///         }
///     }
///
///     @Environment(EnvType.SERVER)
///     public static void spawnFireball(PlayerEntity player) {
///         // Spawn your fireball...
///     }
/// }
/// ```
///
/// > [!IMPORTANT] Server side only!
/// > Should never be called on the client side as:
/// > 1. user disconnection might cause a desync between the server and the client.
/// > 2. Enqueueing important actions on the client is also a very bad habit in terms of cheat detection and prevention.
/// >
/// > For those reasons, any client side call of this method will be ignored.
///
/// ## Members
///
/// ### `enqueue(PlayerEntity, DelayedAction)`
///
/// Enqueues a new [Delayed Action ⌝]() to be run for the `player`.
///
/// **Parameters**
///
/// **`player`**
/// The player to add the delay to.
///
/// **`action`**
/// The action to run.
///
/// ---
///
/// ### `enqueue(PlayerEntity, long, Runnable)`
///
/// Enqueues a new [Delayed Action ⌝]() to be run for the `player`.
///
/// **Parameters**
///
/// **`player`**
/// The player to add the delay to.
///
/// **`delay`**
/// The delay in ticks before the `action` is run.
///
/// **`action`**
/// The action to run.
public interface PlayerDelays {
    /// Enqueues a new {@link DelayedAction} to be run for the `player`.
    ///
    /// @param player The player to add the delay to.
    /// @param action The {@link DelayedAction} to run.
    static void enqueue(PlayerEntity player, DelayedAction action) {
        PlayerDelaysComponent.get(player).enqueue(action);
    }

    /// Enqueues a new {@link DelayedAction} to be run for the `player`. This action will be executed after `delay` ticks.
    ///
    /// @param player The player to add the delay to.
    /// @param delay  The delay in ticks before the `action` is run.
    /// @param action The `Runnable` to be run after `delay` ticks.
    static void enqueue(PlayerEntity player, long delay, Runnable action) {
        enqueue(player, new DelayedAction(player.getEntityWorld().getTime() + delay, delay, action));
    }
}