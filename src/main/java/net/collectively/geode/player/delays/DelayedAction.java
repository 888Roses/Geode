package net.collectively.geode.player.delays;

/// Represents a `Runnable` action to be run after `delay` ticks, or more precisely, when the world time reaches `time`.
///
/// @param time     The world time at which the action should be run.
/// @param delay    The initial delay in ticks before the action is run.
/// @param runnable The action to run.
public record DelayedAction(long time, long delay, Runnable runnable) {
}
