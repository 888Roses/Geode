package net.collectively.geode.v2.mixin.util.interfaces;

import net.collectively.geode.v2.util.interfaces.RandomProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(Particle.class)
public class ParticleMixin implements RandomProvider {
    @Shadow
    @Final
    protected Random random;

    @Override
    public Random random() {
        return random;
    }
}
