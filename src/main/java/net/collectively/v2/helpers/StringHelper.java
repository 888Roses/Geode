package net.collectively.v2.helpers;

import java.util.function.Predicate;

/// Collection of utilities regarding `String` and texts.
@SuppressWarnings("unused")
public interface StringHelper {
    /// Counts the amount of `character` at the start of the given `String`.
    /// @param string The text to check for.
    /// @param character The character to check for.
    /// @return The amount of `character` at the start of the `string`.
    static int countLeading(String string, Predicate<Character> character) {
        for (int i = 0; i < string.length(); i++) if (character.test(string.charAt(i))) return i;
        return 0;
    }

    /// Counts the amount of `character` at the end of the given `String`.
    /// @param string The text to check for.
    /// @param character The character to check for.
    /// @return The amount of `character` at the end of the `string`.
    static int countTailing(String string, Predicate<Character> character) {
        for (int i = string.length() - 1; i >= 0; i--) if (character.test(string.charAt(i))) return i;
        return 0;
    }
}
