package net.collectively.geode.helpers;

import net.collectively.geode.math.math;
import net.collectively.geode.types.double3;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.world.World;
import org.joml.Quaternionf;

/// Collection of rendering oriented utilities.
@SuppressWarnings("unused")
public interface RenderHelper {
    /// Gets the tick delta of the application for that render tick.
    /// @see RenderTickCounter#getTickProgress(boolean)
    @Environment(EnvType.CLIENT)
    static float getTickDelta() {
        MinecraftClient client = MinecraftClient.getInstance();
        RenderTickCounter counter = client.getRenderTickCounter();
        return counter.getTickProgress(true);
    }

    /// Gets the tick delta if the world is client sided. **Otherwise, returns 0.
    /// Unlike [#getTickDelta()] this method is not client sided only**.
    /// @param world The world in which we want to get the tick delta. Mandatory to know whether it is client side or server side.
    /// @return The tick delta or 0 if server side.
    /// @see #getTickDelta()
    static float getTickDeltaOrZero(World world) {
        if (world.isClient()) {
            return getTickDelta();
        }

        return 0;
    }

    /// Creates a new [quaternion][Quaternionf] using the [radians rotation][double3].
    /// @param radians The rotation measured in radians.
    /// @return The created [Quaternionf], to which the rotation was applied using [Quaternionf#rotateXYZ(float, float, float)].
    static Quaternionf rotation(double3 radians) {
        return new Quaternionf().rotateXYZ(
                (float) radians.x(),
                (float) radians.y(),
                (float) radians.z()
        );
    }

    /// Creates a new [quaternion][Quaternionf] using the [degrees rotation][double3].
    /// @param degrees The rotation measured in degrees.
    /// @return The created [Quaternionf], to which the rotation was applied using [Quaternionf#rotateXYZ(float, float, float)].
    /// @see #rotation(double3)
    static Quaternionf rotationDeg(double3 degrees) {
        return rotation(degrees.modify(math::deg2rad));
    }
}
