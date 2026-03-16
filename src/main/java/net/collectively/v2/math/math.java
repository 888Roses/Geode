package net.collectively.v2.math;

import net.minecraft.util.math.MathHelper;

import static net.collectively.v2.math.MathConstants.*;

/**
 * Complete math util lib used through-out Geode itself and available for mods using Geode.
 */
@SuppressWarnings("unused")
public interface math {
    double PI = Math.PI;

    // region range / mapping

    /**
     * Maps the given {@code value} from an {@code in} range to an {@code out} range.
     * @param v the value to remap.
     * @param inMin the lower bound of the original range.
     * @param inMax the upper bound of the original range.
     * @param outMin the lower bound of the desired range.
     * @param outMax the upper bound of the desired range.
     * @return {@code v} remapped in the new range.
     */
    static double remap(double v, double inMin, double inMax, double outMin, double outMax) {
        return ((v - inMin) / (inMax - inMin)) * (outMax - outMin) + outMin;
    }

    /**
     * Maps the given {@code value} from an {@code in} range to an {@code out} range.
     * @param v the value to remap.
     * @param inMin the lower bound of the original range.
     * @param inMax the upper bound of the original range.
     * @param outMin the lower bound of the desired range.
     * @param outMax the upper bound of the desired range.
     * @return {@code v} remapped in the new range.
     */
    static float remap(float v, float inMin, float inMax, float outMin, float outMax) {
        return ((v - inMin) / (inMax - inMin)) * (outMax - outMin) + outMin;
    }

    /**
     * Maps the given {@code value} from an {@code in} range to an {@code out} range.
     * @param v the value to remap.
     * @param inMin the lower bound of the original range.
     * @param inMax the upper bound of the original range.
     * @param outMin the lower bound of the desired range.
     * @param outMax the upper bound of the desired range.
     * @return {@code v} remapped in the new range.
     */
    static int remap(int v, int inMin, int inMax, int outMin, int outMax) {
        return (int) remap((double) v, inMin, inMax, outMin, outMax);
    }

    /**
     * Maps the given {@code value} from an {@code in} range to an {@code out} range.
     * @param v the value to remap.
     * @param inMin the lower bound of the original range.
     * @param inMax the upper bound of the original range.
     * @param outMin the lower bound of the desired range.
     * @param outMax the upper bound of the desired range.
     * @return {@code v} remapped in the new range.
     */
    static long remap(long v, long inMin, long inMax, long outMin, long outMax) {
        return (long) remap((double) v, inMin, inMax, outMin, outMax);
    }

    // endregion

    // region operations

    static double sin(double v) {
        return MathHelper.sin((float) v);
    }

    static float sin(float v) {
        return MathHelper.sin(v);
    }

    static double cos(double v) {
        return MathHelper.cos((float) v);
    }

    static float cos(float v) {
        return MathHelper.cos(v);
    }

    static double sqrt(double v) {
        return Math.sqrt(v);
    }

    static float sqrt(float v) {
        return (float) Math.sqrt(v);
    }

    static double floor(double v) {
        return Math.floor(v);
    }

    static float floor(float v) {
        return (float) Math.floor(v);
    }

    static double ceil(double v) {
        return Math.ceil(v);
    }

    static float ceil(float v) {
        return (float) Math.ceil(v);
    }

    static double round(double v) {
        return Math.round(v);
    }

    static float round(float v) {
        return Math.round(v);
    }

    static double abs(double v) {
        return Math.abs(v);
    }

    static float abs(float v) {
        return Math.abs(v);
    }

    static int abs(int v) {
        return Math.abs(v);
    }

    static long abs(long v) {
        return Math.abs(v);
    }

    static double square(double v) {
        return v * v;
    }

    static float square(float v) {
        return v * v;
    }

    static int square(int v) {
        return v * v;
    }

    static long square(long v) {
        return v * v;
    }

    // endregion

    // region min/max

    static double min(double a, double b) {
        return Math.min(a, b);
    }

    static float min(float a, float b) {
        return Math.min(a, b);
    }

    static int min(int a, int b) {
        return Math.min(a, b);
    }

    static long min(long a, long b) {
        return Math.min(a, b);
    }

    static double max(double a, double b) {
        return Math.max(a, b);
    }

    static float max(float a, float b) {
        return Math.max(a, b);
    }

    static int max(int a, int b) {
        return Math.max(a, b);
    }

    static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * Clamps a value to a range.
     */
    static double clamp(double v, double min, double max) {
        return v < min ? min : Math.min(v, max);
    }

    static float clamp(float v, float min, float max) {
        return v < min ? min : Math.min(v, max);
    }

    static int clamp(int v, int min, int max) {
        return v < min ? min : Math.min(v, max);
    }

    static long clamp(long v, long min, long max) {
        return v < min ? min : Math.min(v, max);
    }

    // endregion

    // region comparison

    /**
     * Tests approximate equality using {@code 1e-5} as a tolerance.
     */
    static boolean approximately(double a, double b) {
        return Math.abs(a - b) < 1e-5;
    }

    /**
     * Tests approximate equality using {@code 1e-5f} as a tolerance.
     */
    static boolean approximately(float a, float b) {
        return Math.abs(a - b) < 1e-5f;
    }

    /**
     * Returns true if {@code value} is divisible by {@code divisor}.
     */
    static boolean isMultipleOf(int value, int divisor) {
        return value % divisor == 0;
    }

    /**
     * Returns true if the value is a power of two.
     */
    static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & (value - 1)) == 0;
    }

    // endregion

    // region angle

    /**
     * Wraps an angle to the range [-180,180].
     */
    static double wrapDegrees(double degrees) {
        double d = degrees % 360;
        if (d >= 180) d -= 360;
        if (d < -180) d += 360;
        return d;
    }

    static float wrapDegrees(float degrees) {
        float d = degrees % 360f;
        if (d >= 180f) d -= 360f;
        if (d < -180f) d += 360f;
        return d;
    }

    /**
     * Converts degrees to radians.
     */
    static double deg2rad(double degrees) {
        return Math.toRadians(degrees);
    }

    /**
     * Converts radians to degrees.
     */
    static double rad2deg(double radians) {
        return Math.toDegrees(radians);
    }

    // endregion

    // region interpolation

    /**
     * Linear interpolation between two values.
     */
    static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    /**
     * Angle interpolation with handling of angle wraparound.
     */
    static double lerpAngle(double t, double a, double b) {
        return a + t * wrapDegrees(b - a);
    }

    static float lerpAngle(float t, float a, float b) {
        return a + t * wrapDegrees(b - a);
    }

    // endregion

    // region distance / mag

    /**
     * Squared hypotenuse of a 2d vector
     */
    static double hypotSquared(double x, double y) {
        return x * x + y * y;
    }

    /**
     * Hypotenuse length of a 2d vector
     */
    static double hypot(double x, double y) {
        return Math.sqrt(hypotSquared(x, y));
    }

    /**
     * Magnitude of a 3d vector
     */
    static double mag(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Chebyshev distance in 2d
     */
    static int chebyshevDistance2(int ax, int ay, int bx, int by) {
        return Math.max(Math.abs(ax - bx), Math.abs(ay - by));
    }

    /**
     * Chebyshev distance in 3d
     */
    static int chebyshevDistance3(int ax, int ay, int az,
                                  int bx, int by, int bz) {
        return Math.max(
                Math.max(Math.abs(ax - bx), Math.abs(ay - by)),
                Math.abs(az - bz)
        );
    }

    /**
     * Distance between a point and a line segment.
     */
    static double distancePoint2Line(double px, double py,
                                     double l1x, double l1y,
                                     double l2x, double l2y) {

        return abs((l2x - l1x) * (l1y - py) - (l1x - px) * (l2y - l1y))
                / sqrt(square(l2x - l1x) + square(l2y - l1y));
    }

    // endregion

    // region geo

    /**
     * Returns pitch/yaw (in radians) facing the given vector.
     */
    static double2 getRadiansRotationFacingVector(double3 vector) {
        double horizontal = vector.horizontalMagnitude();
        return double2.of(MathHelper.atan2(vector.y(), horizontal), MathHelper.atan2(vector.x(), vector.z()));
    }

    /**
     * Returns pitch/yaw (in degrees) facing the given vector
     */
    static double2 getDegreesRotationFacingVector(double3 vector) {
        return getRadiansRotationFacingVector(vector).mod(math::rad2deg);
    }

    // endregion

    // region circle

    static boolean isPointInCircle(double2 c, double radius, double2 p) {
        return square(p.x() - c.x()) + square(p.y() - c.y()) < square(radius);
    }

    static boolean isPointOutOfCircle(double2 c, double radius, double2 p) {
        return square(p.x() - c.x()) + square(p.y() - c.y()) > square(radius);
    }

    static boolean isPointOnPerimeter(double2 c, double radius, double2 p) {
        return square(p.x() - c.x()) + square(p.y() - c.y()) == square(radius);
    }

    // endregion

    // region parse tests

    static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    static boolean isLong(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    // endregion

    // region roman

    /**
     * Converts an integer into its Roman numeral representation
     *
     * @throws IllegalArgumentException if outside supported range: [{@link MathConstants#ROMAN_NUMBER_MIN_VALUE}, {@link MathConstants#ROMAN_NUMBER_MAX_VALUE}]
     */
    static String romanNumber(int number) {
        if (number < ROMAN_NUMBER_MIN_VALUE || number > ROMAN_NUMBER_MAX_VALUE) {
            throw new IllegalArgumentException(
                    String.format("The number must be in the range [%d,%d]",
                            ROMAN_NUMBER_MIN_VALUE,
                            ROMAN_NUMBER_MAX_VALUE));
        }

        return ROMAN_NUMBER_M[number / 1000]
                + ROMAN_NUMBER_C[number % 1000 / 100]
                + ROMAN_NUMBER_X[number % 100 / 10]
                + ROMAN_NUMBER_I[number % 10];
    }

    // endregion
}