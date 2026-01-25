package net.collectively.v2.helpers;

import net.collectively.v1.core.types.double3;
import net.collectively.v1.mc.util.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/// Collection of general [world][World] oriented utilities.
@SuppressWarnings("unused")
public interface WorldHelper {
    /// Contains information about a raymarch instruction.
    ///
    /// @param stepCount How many steps are in this raymarch. Alongside the `increment`, this determines the distance traveled by the raymarch.
    /// @param increment The distance between each step of the raymarch. This is what determines the distance traveled by the raymarch, in combination with the step count.
    /// @param startingRadius The starting length of the sides of the detection box for each step of the raymarch.
    /// @param radiusIncrement Value added to the `startingRadius` at every step of the raymarch, increasing the size of the raymarch detection box to account for distance inaccuracy.
    /// @param validate [predicate][Predicate] of type [Entity] checking if the given raymarched entity is a valid one or not.
    record RaymarchSettings(int stepCount, double increment, double startingRadius, double radiusIncrement, Predicate<Entity> validate) {
    }

    /// Performs a raymarch instruction from the given [starting position][double3] in the [direction][double3] using the [settings][RaymarchSettings]
    /// and returns the [entity][Entity] closest to any step in the raymarch process.
    /// @param world The world in which to perform the raymarch.
    /// @param except An entity to ignore during the raymarching operation. Typically, the caster of the raymarch. Will be ignored if null.
    /// @param direction The direction in which to send the raymarch.
    /// @param startPosition The position to start the raymarch from.
    /// @param settings [Settings][RaymarchSettings] dictating the behavior of the raymarch.
    static @Nullable Entity raymarchToClosest(World world, @Nullable Entity except, double3 direction, double3 startPosition, RaymarchSettings settings) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < settings.stepCount(); i++) {
            double3 pos = startPosition.add(direction.mul(settings.increment() * i));
            double radius = settings.startingRadius() + i * settings.radiusIncrement();

            List<Entity> entities = EntityHelper.getEntitiesAround(except, world, pos, radius);
            for (Entity entity : entities) {
                double distance = entity.getEntityPos().distanceTo(pos.toVec3d());

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        return closestEntity;
    }

    /// Performs a raymarch instruction from the given [starting position][double3] in the [direction][double3] using the
    /// [settings][RaymarchSettings] and returns a [List] of [Entity] containing every hit entity during the raymarch.
    /// @param world The world in which to perform the raymarch.
    /// @param except An entity to ignore during the raymarching operation. Typically, the caster of the raymarch. Will be ignored if null.
    /// @param direction The direction in which to send the raymarch.
    /// @param startPosition The position to start the raymarch from.
    /// @param settings [Settings][RaymarchSettings] dictating the behavior of the raymarch.
    static List<Entity> raymarchAll(World world, @Nullable Entity except, double3 direction, double3 startPosition, RaymarchSettings settings) {
        List<Entity> entities = new ArrayList<>();

        for (int i = 0; i < settings.stepCount(); i++) {
            double3 pos = startPosition.add(direction.mul(settings.increment() * i));
            double radius = settings.startingRadius() + i * settings.radiusIncrement();
            entities.addAll(EntityHelper.getEntitiesAround(except, world, pos, radius));
        }

        return entities;
    }
}