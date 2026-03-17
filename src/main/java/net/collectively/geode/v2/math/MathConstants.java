package net.collectively.geode.v2.math;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MathConstants {
    public static final int ROMAN_NUMBER_MIN_VALUE = 1;
    public static final int ROMAN_NUMBER_MAX_VALUE = 3999;
    public static final String[] ROMAN_NUMBER_M = {"", "M", "MM", "MMM"};
    public static final String[] ROMAN_NUMBER_C = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    public static final String[] ROMAN_NUMBER_X = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    public static final String[] ROMAN_NUMBER_I = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
}
