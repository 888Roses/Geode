package net.collectively.v2.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.collectively.v2.GeodeInternal;
import net.collectively.v2.math.StandardEasingFunction;
import net.minecraft.command.CommandSource;
import net.minecraft.command.suggestion.SuggestionProviders;

import java.util.Arrays;

/// Contains [command source][CommandSource] [suggestion providers][SuggestionProvider] for base Geode types to be used in commands.
@SuppressWarnings("unused")
public interface DefaultSuggestionProviders {
    /// [Suggestion provider][SuggestionProvider] of [command source][CommandSource] providing suggestions for [standard easing functions][StandardEasingFunction] in a command.
    ///
    /// #### Usage
    /// Create an [argument of type `String`][StringArgumentType], and suggest this provider.
    /// It is important to use [StringArgumentType#word()] when creating the argument.
    /// For example:
    /// ```java
    /// .then(CommandManager
    ///     .argument("easing_function", StringArgumentType.word())
    ///     .suggests(SuggestionProvider.cast(DefaultSuggestionProviders.STANDARD_EASING_FUNCTION))
    ///     .executes(ExampleCommand::getEasingFunctionName)
    /// )
    ///
    /// private static int getEasingFunctionName(CommandContext<ServerCommandSource> context) {
    ///     String easingFunctionName = StringArgumentType.getString(context, "easing_function");
    ///     StandardEasingFunction easingFunction = StandardEasingFunction.getFromName(easingFunctionName, null);
    ///
    ///     if (easingFunction == null) {
    ///         return 0;
    ///     }
    ///
    ///     context.getSource().sendMessage(Text.literal("Easing function: " + easingFunction.getName()));
    ///     return 1;
    /// }
    /// ```
    SuggestionProvider<CommandSource> STANDARD_EASING_FUNCTION = SuggestionProviders.register(
            GeodeInternal.internalId("standard_easing_function"),
            (context, builder) ->
                    CommandSource.suggestMatching(
                            Arrays.stream(StandardEasingFunction.values()).map(StandardEasingFunction::getName),
                            builder
                    )
    );
}
