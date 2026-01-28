package net.collectively.geode.helpers;

import net.collectively.geode.math.math;

import java.util.List;

/// Collection of utilities regarding collections, arrays, lists, streams and other enumerable structures.
@SuppressWarnings("unused")
public interface CollectionHelper {
    /// Returns whether the index `i` in within the bounds of the `array` of type [T].
    /// @see #isIn(List, int)
    static <T> boolean isIn(T[] array, int i) {
        return i >= 0 && i < array.length;
    }

    /// Returns whether the index `i` in within the bounds of the [list][List] of type [T].
    /// @see #isIn(Object[], int)
    static <T> boolean isIn(List<T> list, int i) {
        return i >= 0 && i < list.size();
    }

    /// Returns the index `i` clamped between 0 and the length of the `array` of type [T].
    /// @see #restrict(List, int)
    static <T> int restrict(T[] array, int i) {
        return math.clamp(i, 0, array.length - 1);
    }

    /// Returns the index `i` clamped between 0 and the size of the [list][List] of type [T].
    /// @see #restrict(Object[], int)
    static <T> int restrict(List<T> list, int i) {
        return math.clamp(i, 0, list.size() - 1);
    }

    /// Returns the index `i` cycling through the `array` of type [T].
    /// Equivalent of:
    /// ```java
    /// i % list.size()
    /// ```
    ///
    /// @see #cycle(List, int)
    static <T> int cycle(T[] array, int i) {
        return i % array.length;
    }

    /// Returns the index `i` cycling through the [list][List] of type [T].
    /// Equivalent of:
    /// ```java
    /// i % list.size()
    /// ```
    ///
    /// @see #cycle(Object[], int)
    static <T> int cycle(List<T> list, int i) {
        return i % list.size();
    }
}
