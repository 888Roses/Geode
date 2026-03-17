package net.collectively.geode.v2.util.interfaces;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public interface RandomProvider {
    Random random();
    default float nextFloat(float min, float max) {return MathHelper.nextFloat(random(), min, max);}
    default double nextDouble(double min, double max) {return MathHelper.nextDouble(random(), min, max);}
    default int nextInt(int min, int max) {return MathHelper.nextInt(random(), min, max);}
    default long nextLong(long min, long max) {return min >= max ? min : random().nextLong() * (max - min) + min;}
    default boolean nextBoolean() {return random().nextBoolean();}
}
