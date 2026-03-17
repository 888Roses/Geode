package net.collectively.geode.v2.util.interfaces;

import net.collectively.geode.v2.math.double3;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
     * Rotation vector of this entity.
     * @return {@link double3} representing the direction vector the entity is facing.
     * @see Entity#getRotationVector()
     */
    double3 rotation();

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

    /**
     * Sets the stack on cooldown for `duration` ticks.
     * @param itemStack the {@link ItemStack} to put on cooldown.
     * @param duration how long should that stack be on cooldown for.
     * @apiNote this method only works if the entity is a {@link PlayerEntity}.
     */
    void setCooldown(ItemStack itemStack, int duration);

    /**
     * Sets the item's default stack on cooldown for `duration` ticks.
     * @param item the item which {@link Item#getDefaultStack()} should be put on cooldown.
     * @param duration how long should that item be on cooldown for.
     * @apiNote this method only works if the entity is a {@link PlayerEntity}.
     */
    default void setCooldown(Item item, int duration) {
        setCooldown(item.getDefaultStack(), duration);
    }

    /**
     * Whether the given {@link ItemStack} is on cooldown or not.
     * @param itemStack the stack to check for.
     * @apiNote this method only works if the entity is a {@link PlayerEntity}. Otherwise, returns `false`.
     * @return `true` if the stack is on cooldown `false` otherwise.
     */
    boolean isOnCooldown(ItemStack itemStack);

    /**
     * Whether the given {@link Item} is on cooldown or not.
     * @param item the item to check for.
     * @apiNote this method only works if the entity is a {@link PlayerEntity}. Otherwise, returns `false`.
     * @return `true` if the item's {@link Item#getDefaultStack()} is on cooldown `false` otherwise.
     * @see #isOnCooldown(Item)
     */
    default boolean isOnCooldown(Item item) {
        return isOnCooldown(item.getDefaultStack());
    }

    <T extends Entity> List<T> getEntitiesInBox(Class<T> clazz, Box box, Predicate<T> selector);

    default List<Entity> getEntitiesInBox(Box box, Predicate<Entity> selector) {
        return getEntitiesInBox(Entity.class, box, selector);
    }

    <T extends Entity> List<T> getEntitiesInRadius(Class<T> clazz, double radius, Predicate<T> selector);

    default List<Entity> getEntitiesInRadius(double radius, Predicate<Entity> selector) {
        return getEntitiesInRadius(Entity.class, radius, selector);
    }

    double3 velocity();
    void setVelocity(double3 velocity);
    void addVelocity(double3 velocity);
}
