package net.collectively.geode.v2.math;

import net.minecraft.util.math.Vec2f;
import org.joml.Vector2L;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

import java.text.MessageFormat;
import java.util.function.UnaryOperator;

import static net.collectively.geode.v2.math.math.*;

@SuppressWarnings("unused")
public record double2(double x, double y) {
    // region construction

    public static double2 of() {return of(0);}
    public static double2 of(double v) {return of(v, v);}
    public static double2 of(double x, double y) {return new double2(x, y);}
    public static double2 of(Vec2f v) {return new double2(v.x, v.y);}
    public static double2 of(Vector2f v) {return new double2(v.x(), v.y());}
    public static double2 of(net.minecraft.client.util.math.Vector2f v) {return new double2(v.x(), v.y());}
    public static double2 of(Vector2d v) {return new double2(v.x(), v.y());}
    public static double2 of(Vector2i v) {return new double2(v.x(), v.y());}
    public static double2 of(Vector2L v) {return new double2(v.x(), v.y());}

    public static final double2 zero = of(0);
    public static final double2 one = of(1);
    public static final double2 half = of(0.5);
    public static final double2 top = of(0,1);
    public static final double2 down = of(0,-1);
    public static final double2 right = of(1,0);
    public static final double2 left = of(-1,0);
    public static final double2 topRight = of(1,1);
    public static final double2 downRight = of(1,-1);
    public static final double2 topLeft = of(-1,1);
    public static final double2 downLeft = of(-1,-1);

    // endregion

    // region access

    public Vec2f toVec2f() {return new Vec2f((float)x(),(float)y());}
    public net.minecraft.client.util.math.Vector2f toMcVector2f() {return new net.minecraft.client.util.math.Vector2f((float)x(),(float)y());}
    public Vector2f toVector2f() {return new Vector2f((float)x(),(float)y());}
    public Vector2d toVector2d() {return new Vector2d(x(),y());}
    public Vector2i toVector2i() {return new Vector2i((int)x(),(int)y());}
    public Vector2L toVector2L() {return new Vector2L((long)x(), (long)y());}

    public double2 xy() {return of(x(),y());}
    public double2 yx() {return of(y(),x());}
    public double2 xx() {return of(x(),x());}
    public double2 yy() {return of(y(),y());}

    public double2 half() {return of(x()/2d,y()/2d);}
    public double2 quarter() {return of(x()/4d,y()/4d);}

    public boolean isZero() {return x()==0&&y()==0;}
    public boolean isApproximatelyZero(){return approximately(x(),0) && approximately(y(),0);}

    // endregion

    // region mod

    public double2 mod(UnaryOperator<Double> modifier) {return of(modifier.apply(x()),modifier.apply(y()));}
    public double2 modx(UnaryOperator<Double> modifier) {return of(modifier.apply(x()),y());}
    public double2 mody(UnaryOperator<Double> modifier) {return of(x(),modifier.apply(y()));}

    public double2 wx(double x) {return of(x, y());}
    public double2 wy(double y) {return of(x(), y);}

    // endregion

    // region math

    public double squaredMagnitude() {return square(x()) + square(y());}
    public double magnitude() {return sqrt(squaredMagnitude());}

    public double2 add(double2 v) {return of(x() + v.x(), y() + v.y());}
    public double2 add(Vec2f v) {return of(x() + v.x, y() + v.y);}
    public double2 add(net.minecraft.client.util.math.Vector2f v) {return of(x() + v.x(), y() + v.y());}
    public double2 add(Vector2f v) {return of(x() + v.x(), y() + v.y());}
    public double2 add(Vector2d v) {return of(x() + v.x(), y() + v.y());}
    public double2 add(Vector2i v) {return of(x() + v.x(), y() + v.y());}
    public double2 add(Vector2L v) {return of(x() + v.x(), y() + v.y());}
    public double2 addx(double v) {return of(x() + v, y());}
    public double2 addy(double v) {return of(x(), y() + v);}

    public double2 sub(double2 v) {return of(x() - v.x(), y() - v.y());}
    public double2 sub(Vec2f v) {return of(x() - v.x, y() - v.y);}
    public double2 sub(net.minecraft.client.util.math.Vector2f v) {return of(x() - v.x(), y() - v.y());}
    public double2 sub(Vector2f v) {return of(x() - v.x(), y() - v.y());}
    public double2 sub(Vector2d v) {return of(x() - v.x(), y() - v.y());}
    public double2 sub(Vector2i v) {return of(x() - v.x(), y() - v.y());}
    public double2 sub(Vector2L v) {return of(x() - v.x(), y() - v.y());}
    public double2 subx(double v) {return of(x() - v, y());}
    public double2 suby(double v) {return of(x(), y() - v);}

    public double2 mul(double2 v) {return of(x() * v.x(), y() * v.y());}
    public double2 mul(Vec2f v) {return of(x() * v.x, y() * v.y);}
    public double2 mul(net.minecraft.client.util.math.Vector2f v) {return of(x() * v.x(), y() * v.y());}
    public double2 mul(Vector2f v) {return of(x() * v.x(), y() * v.y());}
    public double2 mul(Vector2d v) {return of(x() * v.x(), y() * v.y());}
    public double2 mul(Vector2i v) {return of(x() * v.x(), y() * v.y());}
    public double2 mul(Vector2L v) {return of(x() * v.x(), y() * v.y());}
    public double2 mulx(double v) {return of(x() * v, y());}
    public double2 muly(double v) {return of(x(), y() * v);}

    public double2 div(double2 v) {return of(x() / v.x(), y() / v.y());}
    public double2 div(Vec2f v) {return of(x() / v.x, y() / v.y);}
    public double2 div(net.minecraft.client.util.math.Vector2f v) {return of(x() / v.x(), y() / v.y());}
    public double2 div(Vector2f v) {return of(x() / v.x(), y() / v.y());}
    public double2 div(Vector2d v) {return of(x() / v.x(), y() / v.y());}
    public double2 div(Vector2i v) {return of(x() / v.x(), y() / v.y());}
    public double2 div(Vector2L v) {return of(x() / v.x(), y() / v.y());}
    public double2 divx(double v) {return of(x() / v, y());}
    public double2 divy(double v) {return of(x(), y() / v);}

    public double2 lerpTo(double delta, double2 end) {return of(lerp(delta, x(), end.x()),lerp(delta, y(), end.y()));}
    public double2 lerpTo(double delta, Vec2f end) {return lerpTo(delta, of(end));}
    public double2 lerpTo(double delta, net.minecraft.client.util.math.Vector2f end) {return lerpTo(delta, of(end));}
    public double2 lerpTo(double delta, Vector2f end) {return lerpTo(delta, of(end));}
    public double2 lerpTo(double delta, Vector2d end) {return lerpTo(delta, of(end));}
    public double2 lerpTo(double delta, Vector2i end) {return lerpTo(delta, of(end));}
    public double2 lerpTo(double delta, Vector2L end) {return lerpTo(delta, of(end));}

    public double2 lerpFrom(double delta, double2 end) {return end.lerpTo(delta, this);}
    public double2 lerpFrom(double delta, Vec2f end) {return lerpFrom(delta, of(end));}
    public double2 lerpFrom(double delta, net.minecraft.client.util.math.Vector2f end) {return lerpFrom(delta, of(end));}
    public double2 lerpFrom(double delta, Vector2f end) {return lerpFrom(delta, of(end));}
    public double2 lerpFrom(double delta, Vector2d end) {return lerpFrom(delta, of(end));}
    public double2 lerpFrom(double delta, Vector2i end) {return lerpFrom(delta, of(end));}
    public double2 lerpFrom(double delta, Vector2L end) {return lerpFrom(delta, of(end));}

    // endregion

    // region string

    @Override
    public @NonNull String toString() {
        return MessageFormat.format("[{0}, {1}]", x, y);
    }

    // endregion
}
