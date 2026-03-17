package net.collectively.geode.v2.mixin.util.interfaces;

import net.collectively.geode.v2.util.interfaces.RandomProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(World.class)
public abstract class WorldMixin implements RandomProvider {
    @Shadow
    public abstract Random getRandom();

    @Override public Random random() {return getRandom();}
}
