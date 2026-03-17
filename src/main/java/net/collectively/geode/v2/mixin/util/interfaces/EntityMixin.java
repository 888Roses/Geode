package net.collectively.geode.v2.mixin.util.interfaces;

import net.collectively.geode.v2.math.double3;
import net.collectively.geode.v2.util.interfaces.EntityGetter;
import net.collectively.geode.v2.util.interfaces.RandomProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Entity.class)
public abstract class EntityMixin implements EntityGetter, RandomProvider {
    @Shadow
    public abstract Vec3d getEntityPos();

    @Shadow
    public abstract World getEntityWorld();

    @Shadow
    public abstract float getEyeHeight(EntityPose pose);

    @Shadow
    public abstract EntityPose getPose();

    @Shadow
    public abstract Vec3d getEyePos();

    @Shadow
    public double lastX;

    @Shadow
    public double lastY;

    @Shadow
    public double lastZ;

    @Shadow
    public abstract Random getRandom();

    @Override
    public double3 pos() {
        return double3.of(getEntityPos());
    }

    @Override
    public double3 lastPos() {
        return double3.of(lastX, lastY, lastZ);
    }

    @Override
    public double3 eyePos() {
        return double3.of(getEyePos());
    }

    @Override
    public double3 prevEyePos() {
        return eyePos().add(getEyeHeight(getPose()));
    }

    @Override
    public double3 centerPos() {
        return pos().lerpTo(0.5, eyePos());
    }

    @Override
    public boolean isClient() {
        return getEntityWorld().isClient();
    }

    @Override
    public Random random() {
        return getRandom();
    }
}
