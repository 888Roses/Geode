package net.collectively.v2.helpers;

import net.collectively.v2.types.double3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/// Collection of utilities regarding entities.
@SuppressWarnings("unused")
public interface EntityHelper {
    /// Returns whether the given [hand][Hand] is the main arm of the [player][PlayerLikeEntity] or not.
    /// @param playerLike The checked player.
    /// @param hand The hand we want to check for.
    /// @return True if the given hand corresponds to the [main arm][PlayerLikeEntity#getMainArm()] of the player, false otherwise.
    static boolean isMainHand(PlayerLikeEntity playerLike, Hand hand) {
        return (hand == Hand.MAIN_HAND) == (playerLike.getMainArm() == Arm.RIGHT);
    }

    /// Gets the [forward direction][Entity#getRotationVector()] of the given [entity][Entity] as a [double3].
    /// @param entity The entity we want to get the forward direction of.
    /// @return The forward direction of the entity as a [double3].
    static double3 forward(Entity entity) {
        return new double3(entity.getRotationVector());
    }

    /// Gets the previous position of the [entity][Entity] as a [double3].
    /// @param entity The entity we want to get the last position of.
    /// @return A new [double3] made from the last [x][Entity#lastX], [y][Entity#lastY], and [z][Entity#lastZ] of the entity.
    static double3 getLastPosition(Entity entity) {
        return new double3(entity.lastX, entity.lastY, entity.lastZ);
    }

    /// Gets the [eye position][Entity#getEyePos()] of the given [entity][Entity] as a [double3].
    /// @param entity The entity we want to get the eye position of.
    /// @return A new [double3] containing the [eye position][Entity#getEyePos()] of the entity.
    static double3 getEyePosition(Entity entity) {
        return new double3(entity.getEyePos());
    }

    /// Gets the previous [eye position][Entity#getEyePos()] of the given [entity][Entity] as a [double3].
    /// @param entity The entity we want to get the last eye position of.
    /// @return A new [double3] containing the [eye position][Entity#getEyePos()] of the entity in the last frame.
    static double3 getLastEyePosition(Entity entity) {
        return getLastPosition(entity).addY(entity.getEyeHeight(entity.getPose()));
    }

    /// Gets every [entity][Entity] around a given [source entity][Entity] in the given `radius`.
    ///
    /// @param sourceEntity The entity containing the world, and representing the pivot point for the detection. It is also
    ///                     excluded when selecting nearby entities.
    /// @param radius       The length of the detection box.
    /// @return             A [list][List] of [Entity] containing every valid found target.
    static List<Entity> getEntitiesAround(Entity sourceEntity, double radius) {
        return getEntitiesAround(sourceEntity, radius, target -> true);
    }

    /// Gets every [entity][Entity] around a given [source entity][Entity] in the given `radius` while respecting the
    /// [validation predicate][Predicate] of type [Entity].
    ///
    /// @param sourceEntity The entity containing the world, and representing the pivot point for the detection. It is also
    ///                     excluded when selecting nearby entities.
    /// @param radius       The length of the detection box.
    /// @param validate     A [predicate][Predicate] to check whether the entity is a valid target or not.
    /// @return             A [list][List] of [Entity] containing every valid found target.
    static List<Entity> getEntitiesAround(Entity sourceEntity, double radius, Predicate<Entity> validate) {
        return getEntitiesAround(sourceEntity, sourceEntity.getEntityWorld(), new double3(sourceEntity.getEntityPos()), radius, validate);
    }

    /// Gets every [entity][Entity] around a given [point][double3] in the [world][World] in the given `radius` while
    /// respecting the [validation predicate][Predicate] of type [Entity].
    ///
    /// @param except   An entity to exclude when selecting nearby entities.
    /// @param world    The world in which we're checking.
    /// @param point    The center of the detection box.
    /// @param radius   The length of the detection box.
    /// @return         A [list][List] of [Entity] containing every valid found target.
    static List<Entity> getEntitiesAround(@Nullable Entity except, World world, double3 point, double radius) {
        return getEntitiesAround(except, world, point, radius, target -> true);
    }

    /// Gets every [entity][Entity] around a given [point][double3] in the [world][World] in the given `radius` while
    /// respecting the [validation predicate][Predicate] of type [Entity].
    ///
    /// @param except   An entity to exclude when selecting nearby entities.
    /// @param world    The world in which we're checking.
    /// @param point    The center of the detection box.
    /// @param radius   The length of the detection box.
    /// @param validate A [predicate][Predicate] to check whether the entity is a valid target or not.
    /// @return         A [list][List] of [Entity] containing every valid found target.
    static List<Entity> getEntitiesAround(@Nullable Entity except, World world, double3 point, double radius, Predicate<Entity> validate) {
        Vec3d pointAsVec3d = point.toVec3d();
        Box box = Box.of(pointAsVec3d, radius, radius, radius);
        double squaredRadius = radius * radius;

        return world.getEntitiesByClass(Entity.class, box, targetEntity -> targetEntity != except && validate.test(targetEntity))
                .stream().filter(targetEntity -> targetEntity.squaredDistanceTo(pointAsVec3d) <= squaredRadius).toList();
    }
}