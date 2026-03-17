package net.collectively.geode.v2.util.interfaces;

import net.collectively.geode.v2.math.double3;

public interface EntityGetter {
    double3 pos();
    double3 lastPos();
    double3 eyePos();
    double3 prevEyePos();
    double3 centerPos();
    boolean isClient();
}
