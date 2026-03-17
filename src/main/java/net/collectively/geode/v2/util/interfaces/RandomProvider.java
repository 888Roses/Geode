package net.collectively.geode.v2.util.interfaces;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface RandomProvider {
    /**
     * Retrieves the random engine of this provider.
     * @return {@link Random} engine used to generate the random values.
     */
    Random random();

    /**
     * Generates an uniform random `float` between `min` and `max`.
     * @param min lower bound.
     * @param max upper bound.
     * @return an uniformly randomized `float` between `min` and `max`.
     */
    default float nextFloat(float min, float max) {return MathHelper.nextFloat(random(), min, max);}

    /**
     * Generates an uniform random `double` between `min` and `max`.
     * @param min lower bound.
     * @param max upper bound.
     * @return an uniformly randomized `double` between `min` and `max`.
     */
    default double nextDouble(double min, double max) {return MathHelper.nextDouble(random(), min, max);}

    /**
     * Generates an uniform random `int` between `min` and `max`.
     * @param min lower bound.
     * @param max upper bound.
     * @return an uniformly randomized `int` between `min` and `max`.
     */
    default int nextInt(int min, int max) {return MathHelper.nextInt(random(), min, max);}

    /**
     * Generates an uniform random `long` between `min` and `max`.
     * @param min lower bound.
     * @param max upper bound.
     * @return an uniformly randomized `long` between `min` and `max`.
     */
    default long nextLong(long min, long max) {return min >= max ? min : random().nextLong() * (max - min) + min;}

    /**
     * Generates an uniform random `boolean`.
     * @return an uniformly randomized `boolean`.
     */
    default boolean nextBoolean() {return random().nextBoolean();}
}
