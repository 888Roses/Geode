package net.collectively.geode.v2.mixin.util.interfaces;

import net.collectively.geode.v2.math.double3;
import net.collectively.geode.v2.util.interfaces.EntityGetter;
import net.collectively.geode.v2.util.interfaces.RandomProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    @Shadow
    public abstract Vec3d getRotationVector();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract void addVelocity(Vec3d vec);

    @Shadow
    public abstract Vec3d getVelocity();

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
    public double3 lastEyePos() {
        return eyePos().add(getEyeHeight(getPose()));
    }

    @Override
    public double3 rotation() {
        return double3.of(getRotationVector());
    }

    @Override
    public boolean isClient() {
        return getEntityWorld().isClient();
    }

    @Override
    public Random random() {
        return getRandom();
    }

    @Override
    public boolean message(Text text, boolean aboveHotbar) {
        if ((Entity) (Object) this instanceof PlayerEntity player) {
            player.sendMessage(text, aboveHotbar);
            return true;
        }

        return false;
    }

    @Override
    public void setCooldown(ItemStack itemStack, int duration) {
        if ((Entity) (Object) this instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(itemStack, duration);
        }
    }

    @Override
    public boolean isOnCooldown(ItemStack itemStack) {
        if ((Entity) (Object) this instanceof PlayerEntity player) {
            return player.getItemCooldownManager().isCoolingDown(itemStack);
        }

        return false;
    }

    @Override
    public <T extends Entity> List<T> getEntitiesInBox(Class<T> clazz, Box box, Predicate<T> selector) {
        return getEntityWorld().getEntitiesByClass(clazz, box, selector);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesInRadius(Class<T> clazz, double radius, Predicate<T> selector) {
        return getEntitiesInBox(
                clazz,
                Box.of(getEntityPos(), radius * 2, radius * 2, radius * 2),
                entity -> entity.distanceTo((Entity) (Object) this) <= radius && selector.test(entity)
        );
    }

    @Override
    public void setVelocity(double3 velocity) {
        setVelocity(velocity.toVec3d());
    }

    @Override
    public void addVelocity(double3 velocity) {
        addVelocity(velocity.toVec3d());
    }

    @Override
    public double3 velocity() {
        return double3.of(getVelocity());
    }
}
