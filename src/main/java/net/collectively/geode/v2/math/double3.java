package net.collectively.geode.v2.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.*;
import org.jspecify.annotations.NonNull;

import java.lang.Math;
import java.text.MessageFormat;
import java.util.function.UnaryOperator;
import static net.collectively.geode.v2.math.math.*;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public record double3(double x, double y, double z) implements Position, Comparable<Position> {
    //region construction

    public static double3 of() {return of(0);}
    public static double3 of(double x) {return new double3(x,x,x);}
    public static double3 of(double x, double y) {return new double3(x,y,0);}
    public static double3 of(double x, double y, double z) {return new double3(x,y,z);}
    public static double3 of(Position position) {return of(position.getX(), position.getY(), position.getZ());}
    public static double3 ofCentered(BlockPos blockPos) {return of(blockPos.toCenterPos());}
    public static double3 of(Vec3i v) {return of(v.getX(), v.getY(), v.getZ());}
    public static double3 of(Vector3f v) {return of(v.x, v.y, v.z);}
    public static double3 of(Vector3d v) {return of(v.x, v.y, v.z);}
    public static double3 of(Vector3i v) {return of(v.x(), v.y(), v.z());}
    public static double3 of(Vector3L v) {return of(v.x(), v.y(), v.z());}

    public static final double3 zero = of(0);
    public static final double3 one = of(1);
    public static final double3 half = of(0.5);
    public static final double3 middle = half;
    public static final double3 east = of(1, 0, 0);
    public static final double3 west = of(-1, 0, 0);
    public static final double3 up = of(0, 1, 0);
    public static final double3 down = of(0, -1, 0);
    public static final double3 north = of(0, 0, 1);
    public static final double3 south = of(0, 0, -1);
    public static final double3 eastUp = of(1, 1, 0);
    public static final double3 eastDown = of(1, -1, 0);
    public static final double3 westUp = of(-1, 1, 0);
    public static final double3 westDown = of(-1, -1, 0);
    public static final double3 northUp = of(0, 1, 1);
    public static final double3 northDown = of(0, -1, 1);
    public static final double3 southUp = of(0, 1, -1);
    public static final double3 southDown = of(0, -1, -1);
    public static final double3 northEastUp = one;
    public static final double3 northWestUp = of(-1, 1, 1);
    public static final double3 northEastDown = of(1, -1, 1);
    public static final double3 northWestDown = of(-1, -1, 1);
    public static final double3 southEastUp = of(1, 1, -1);
    public static final double3 southWestUp = of(-1, 1, -1);
    public static final double3 southEastDown = of(1, -1, -1);
    public static final double3 southWestDown = of(-1, -1, -1);

    //endregion

    //region access

    public Vec3d toVec3d() {return new Vec3d(x,y,z);}
    public BlockPos toBlockPos() {return BlockPos.ofFloored(x,y,z);}
    public Vector3f toVector3f() {return new Vector3f((float)x,(float)y,(float)z);}
    public Vector3d toVector3d() {return new Vector3d(x,y,z);}
    public Vector3i toVector3i() {return new Vector3i((int)x,(int)y,(int)z);}
    public Vector3L toVector3L() {return new Vector3L((int)x,(int)y,(int)z);}
    public Vec3i toVec3i() {return new Vec3i((int)x,(int)y,(int)z);}

    public double2 xy() {return double2.of(x,y);}
    public double2 xz() {return double2.of(x,z);}
    public double2 yx() {return double2.of(y,x);}
    public double2 yz() {return double2.of(y,z);}
    public double2 zx() {return double2.of(z,x);}
    public double2 zy() {return double2.of(z,y);}

    public double2 xx() {return double2.of(x,x);}
    public double2 yy() {return double2.of(y,y);}
    public double2 zz() {return double2.of(z,z);}

    public double3 xyz() {return of(x,y,z);}
    public double3 xzy() {return of(x,z,y);}
    public double3 yxz() {return of(y,x,z);}
    public double3 yzx() {return of(y,z,x);}
    public double3 zxy() {return of(z,x,y);}
    public double3 zyx() {return of(z,y,x);}

    public double3 half() {return of(x/2d,y/2d,z/2d);}
    public double3 quarter() {return of(x/4d,y/4d,z/4d);}

    public boolean isZero() {return x == 0 && y == 0 && z == 0;}
    public boolean isApproximatelyZero(){return approximately(x(),0) && approximately(y(),0) && approximately(z(),0);}

    //endregion

    // region modify

    public double3 mod(UnaryOperator<Double> modifier) {return of(modifier.apply(x), modifier.apply(y), modifier.apply(z));}
    public double3 modx(UnaryOperator<Double> modifier) {return of(modifier.apply(x),y,z);}
    public double3 mody(UnaryOperator<Double> modifier) {return of(x,modifier.apply(y),z);}
    public double3 modz(UnaryOperator<Double> modifier) {return of(x,y,modifier.apply(z));}

    public double3 wx(double x) {return of(x,y,z);}
    public double3 wy(double y) {return of(x,y,z);}
    public double3 wz(double z) {return of(x,y,z);}

    // endregion

    // region position

    @Override public double getX() { return this.x; }
    @Override public double getY() { return this.y; }
    @Override public double getZ() { return this.z; }

    // endregion

    // region comparable

    @Override
    public int compareTo(Position other) {
        if (this.getY() == other.getY()) {
            return (int) Math.round(this.getZ() == other.getZ() ? this.getX() - other.getX() : this.getZ() - other.getZ());
        }

        return (int) Math.round(this.getY() - other.getY());
    }

    // endregion

    // region math

    public double squaredMagnitude() {return square(x) + square(y) + square(z);}
    public double squaredHorizontalMagnitude() {return xz().squaredMagnitude();}
    public double magnitude() {return sqrt(squaredMagnitude());}
    public double horizontalMagnitude() {return sqrt(squaredHorizontalMagnitude());}
    public double3 normalize() {
        return div(sqrt(magnitude()));
    }

    public double3 add(double3 v) {return of(x+v.x(),y+v.y(),z+v.z());}
    public double3 add(Vector3f v) {return of(x+v.x(),y+v.y(),z+v.z());}
    public double3 add(Vector3d v) {return of(x+v.x(),y+v.y(),z+v.z());}
    public double3 add(Vector3i v) {return of(x+v.x(),y+v.y(),z+v.z());}
    public double3 add(Vector3L v) {return of(x+v.x(),y+v.y(),z+v.z());}
    public double3 add(Position v) {return of(x+v.getX(),y+v.getY(),z+v.getZ());}
    public double3 add(Vec3i v) {return of(x+v.getX(),y+v.getY(),z+v.getZ());}
    public double3 add(double v) {return add(of(v));}
    public double3 addx(double v){return add(of(v,0,0));}
    public double3 addy(double v){return add(of(0,v,0));}
    public double3 addz(double v){return add(of(0,0,v));}

    public double3 sub(double3 v) {return of(x-v.x(),y-v.y(),z-v.z());}
    public double3 sub(Vector3f v) {return of(x-v.x(),y-v.y(),z-v.z());}
    public double3 sub(Vector3d v) {return of(x-v.x(),y-v.y(),z-v.z());}
    public double3 sub(Vector3i v) {return of(x-v.x(),y-v.y(),z-v.z());}
    public double3 sub(Vector3L v) {return of(x-v.x(),y-v.y(),z-v.z());}
    public double3 sub(Position v) {return of(x-v.getX(),y-v.getY(),z-v.getZ());}
    public double3 sub(Vec3i v) {return of(x-v.getX(),y-v.getY(),z-v.getZ());}
    public double3 sub(double v) {return sub(of(v));}
    public double3 subx(double v){return sub(of(v,0,0));}
    public double3 suby(double v){return sub(of(0,v,0));}
    public double3 subz(double v){return sub(of(0,0,v));}

    public double3 mul(double3 v) {return of(x*v.x(),y*v.y(),z*v.z());}
    public double3 mul(Vector3f v) {return of(x*v.x(),y*v.y(),z*v.z());}
    public double3 mul(Vector3d v) {return of(x*v.x(),y*v.y(),z*v.z());}
    public double3 mul(Vector3i v) {return of(x*v.x(),y*v.y(),z*v.z());}
    public double3 mul(Vector3L v) {return of(x*v.x(),y*v.y(),z*v.z());}
    public double3 mul(Position v) {return of(x*v.getX(),y*v.getY(),z*v.getZ());}
    public double3 mul(Vec3i v) {return of(x*v.getX(),y*v.getY(),z*v.getZ());}
    public double3 mul(double v) {return mul(of(v));}
    public double3 mulx(double v){return mul(of(v,1,1));}
    public double3 muly(double v){return mul(of(1,v,1));}
    public double3 mulz(double v){return mul(of(1,1,v));}

    public double3 div(double3 v) {return of(x/v.x(),y/v.y(),z/v.z());}
    public double3 div(Vector3f v) {return of(x/v.x(),y/v.y(),z/v.z());}
    public double3 div(Vector3d v) {return of(x/v.x(),y/v.y(),z/v.z());}
    public double3 div(Vector3i v) {return of(x/v.x(),y/v.y(),z/v.z());}
    public double3 div(Vector3L v) {return of(x/v.x(),y/v.y(),z/v.z());}
    public double3 div(Position v) {return of(x/v.getX(),y/v.getY(),z/v.getZ());}
    public double3 div(Vec3i v) {return of(x/v.getX(),y/v.getY(),z/v.getZ());}
    public double3 div(double v) {return div(of(v));}
    public double3 divx(double v){return div(of(v,1,1));}
    public double3 divy(double v){return div(of(1,v,1));}
    public double3 divz(double v){return div(of(1,1,v));}

    // endregion

    // region string

    @Override
    public @NonNull String toString() {
        return MessageFormat.format("[{0}, {1}, {2}]", x, y, z);
    }

    // endregion
}
