package dev.rosenoire.testmod;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.collectively.v2.commands.DefaultSuggestionProviders;
import net.collectively.v2.math.StandardEasingFunction;
import net.collectively.v2.text.TextBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                        .then(CommandManager.literal("text_builder")
                                .then(CommandManager.literal("basic").executes(TestingModCommands::testGTextBasic)))
                ))
        );
    }

    static int testGTextBasic(CommandContext<ServerCommandSource> ctx) {
        ctx.getSource().sendMessage(
                TextBuilder.of()
                        .text("Formatting ").formatting(Formatting.RED)
                        .text("Color ").color(0x99aa44)
                        .text("Shadow Color ").shadowColor(0xffff55ff)
                        .text("Bold ").bold(true)
                        .text("Italic ").italic(true)
                        .text("Underline ").underline(true)
                        .text("Strikethrough ").strikethrough(true)
                        .text("Obfuscated ").obfuscated(true)
                        .text("Click Event ").clickEvent(new ClickEvent.SuggestCommand("/say Hello, World!"))
                        .text("Hover Event ").hoverEvent(new HoverEvent.ShowText(Text.literal("Hello! Hover Event!")))
                        .build()
        );
        return 1;
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
