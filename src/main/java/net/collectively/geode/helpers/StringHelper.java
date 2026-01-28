package net.collectively.geode.helpers;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /// Takes in an identifier-like `String` (lowercase only, no whitespace, words delimited by `_` characters) and
    /// transforms it into a human-readable name. For example:
    /// ```java
    /// String identifier = "example_identifier";
    /// System.out.println(StringHelper.toHumanName(identifier));
    /// 
    /// // [Console]
    /// // Example identifier
    /// ```
    /// 
    /// @param identifier Identifier pattern `String` to transform into a human-readable text.
    /// @return           The human-readable text.
    /// @apiNote          The given identifier `String` is not checked. You are responsible for making sure it respects the
    ///                   aforementioned identifier characteristics.
    /// @see #toHumanReadableName(Identifier)
    static String toHumanReadableName(String identifier) {
        return Arrays.stream(identifier.split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }
    
    /// Takes in an [identifier][Identifier] and returns a human-readable name created using its [path][Identifier#path].
    /// For example:
    /// ```java
    /// Identifier identifier = Identifier.of("example_mod", "op_item"):
    /// System.out.println(StringHelper.toHumanName(identifier));
    /// 
    /// // [Console]
    /// // Op Item
    /// ```
    /// @param identifier The identifier whose path is going to be transformed into a human-readable text.
    /// @return The human-readable text.
    /// @see #toHumanReadableName(String)
    static String toHumanReadableName(@NotNull Identifier identifier) {
        return toHumanReadableName(identifier.getPath());
    }
}
