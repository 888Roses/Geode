package dev.rosenoire.testmod;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.collectively.v2.commands.DefaultSuggestionProviders;
import net.collectively.v2.math.StandardEasingFunction;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public interface TestingModCommands {
    String EASING_FUNCTION = "easing_function";

    static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> commandDispatcher.register(CommandManager
                .literal("geode_test")
                .then(CommandManager
                        .literal("suggestion_providers")
                        .then(CommandManager.literal("easing_function").then(CommandManager
                                .argument(EASING_FUNCTION, StringArgumentType.word())
                                .suggests(SuggestionProviders.cast(DefaultSuggestionProviders.STANDARD_EASING_FUNCTION))
                                .executes(TestingModCommands::testSuggestionProviders)
                        ))
                ))
        );
    }

    static int testSuggestionProviders(CommandContext<ServerCommandSource> ctx) {
        String easingFunctionName = StringArgumentType.getString(ctx, EASING_FUNCTION);
        StandardEasingFunction easingFunction = StandardEasingFunction.getFromName(easingFunctionName, null);

        if (easingFunction == null) {
            return 0;
        }

        ctx.getSource().sendMessage(Text.literal("Easing function: " + easingFunction.getName()));
        return 1;
    }
}
