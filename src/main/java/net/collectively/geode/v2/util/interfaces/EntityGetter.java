package net.collectively.geode.v2.util.interfaces;

import net.collectively.geode.v2.math.double3;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.entity.player.PlayerEntity;

@ApiStatus.NonExtendable
public interface EntityGetter {
    /**
     * Position of the entity.
     * @return {@link double3} representing the position of the entity.
     */
    double3 pos();

    /**
     * Position of the entity during the previous game tick.
     * @return {@link double3} representing the position of the entity during the previous game tick.
     */
    double3 lastPos();

    /**
     * Position of the eyes of the entity. This position is in World space.
     * @return {@link double3} representing the World position of the eyes of the entity.
     * @see #localEyePos()
     */
    double3 eyePos();

    /**
     * Position of the eyes of the entity. This position is in Local space relatively to the entity's position.
     * @return {@link double3} representing the local position of the eyes of the entity.
     * @see #eyePos()
     */
    default double3 localEyePos() {
        return eyePos().sub(pos());
    }

    /**
     * Attempts to retrieve the position of the eyes of the entity during the previous game tick.
     * @apiNote This method has a 1 tick inaccuracy when the entity just changed {@link Entity#getPose()}. This position is in World space.
     * @return {@link double3} representing the position of the eyes of the entity during the previous game tick.
     * @see #lastLocalEyePos()
     */
    double3 lastEyePos();

    /**
     * Attempts to retrieve the position of the eyes of the entity during the previous game tick.
     * @apiNote This method has a 1 tick inaccuracy when the entity just changed {@link Entity#getPose()}. This position is in Local space.
     * @return {@link double3} representing the position of the eyes of the entity during the previous game tick.
     * @see #lastEyePos()
     */
    default double3 lastLocalEyePos() {
        return lastEyePos().sub(lastPos());
    }

    /**
     * Position in the middle of the entity's {@link #pos()} and {@link #eyePos()}.
     * @return {@link double3} representing the center of the {@link #pos()} and {@link #eyePos()} of the entity.
     */
    default double3 centerPos() {
        return pos().lerpTo(0.5, eyePos());
    }

    /**
     * Attempts to retrieve the position in the middle of the entity's {@link #lastPos()} and {@link #lastEyePos()}.
     * @apiNote Since it uses {@link #lastEyePos()} this method has a 1 tick inaccuracy when it comes to accurately determining the last eye position.
     * @return {@link double3} representing the center of the {@link #lastPos()} and {@link #lastEyePos()} of the entity.
     */
    default double3 lastCenterPos() {
        return lastPos().lerpTo(0.5, lastEyePos());
    }

    /**
     * Whether this entity is on Client side.
     * @return `true` if this entity is on Client side `false` otherwise.
     * @see World#isClient()
     */
    boolean isClient();

    /**
     * Sends a message in the chat of the entity if it is a {@link PlayerEntity}.
     * @param text the {@link Text} to send in chat.
     * @param aboveHotbar whether to send the message in the chat of the user, or above the hotbar.
     * @return `true` if the message could be sent, `false` otherwise.
     * @see PlayerEntity#sendMessage(Text, boolean)
     */
    boolean message(Text text, boolean aboveHotbar);

    /**
     * Sends a message in the chat of the entity if it is a {@link PlayerEntity}.
     * @param text the {@link Text} to send in chat.
     * @return `true` if the message could be sent, `false` otherwise.
     * @see PlayerEntity#sendMessage(Text, boolean)
     */
    default boolean message(Text text) {
        return message(text, false);
    }

    /**
     * Sends a message in the chat of the entity if it is a {@link PlayerEntity}.
     * @param text the `String` to send in chat.
     * @param aboveHotbar whether to send the message in the chat of the user, or above the hotbar.
     * @return `true` if the message could be sent, `false` otherwise.
     * @see PlayerEntity#sendMessage(Text, boolean)
     */
    default boolean message(String text, boolean aboveHotbar) {
        return message(Text.literal(text), aboveHotbar);
    }

    /**
     * Sends a message in the chat of the entity if it is a {@link PlayerEntity}.
     * @param text the `String` to send in chat.
     * @return `true` if the message could be sent, `false` otherwise.
     * @see PlayerEntity#sendMessage(Text, boolean)
     */
    default boolean message(String text) {
        return message(text, false);
    }
}
