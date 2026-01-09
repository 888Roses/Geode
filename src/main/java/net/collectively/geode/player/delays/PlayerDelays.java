package net.collectively.geode.player.delays;

import net.minecraft.entity.player.PlayerEntity;

/// Offers a way of enqueuing actions to be run after a given tick delay on both sides. Example use:
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
///     public static void spawnFireball(PlayerEntity player) {
///         // Spawn your fireball...
///     }
/// }
/// ```
///
/// > [!IMPORTANT]
/// > Enqueueing actions on the client side is risky. Since they run on the client side, a player leaving and re-joining the
/// server will clear all enqueued actions since `Runnable` isn't savable. This means that enqueueing important gameplay
/// delayed actions on the server, and then sending them to the client using payloads is always the preferred approach.
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
