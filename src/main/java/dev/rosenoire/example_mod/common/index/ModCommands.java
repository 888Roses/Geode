package dev.rosenoire.example_mod.common.index;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.collectively.v2.commands.DefaultSuggestionProviders;
import net.collectively.v2.helpers.RegistryHelper;
import net.collectively.v2.helpers.StringHelper;
import net.collectively.v2.math.StandardEasingFunction;
import net.collectively.v2.text.TextBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.registry.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface ModCommands {
    String EASING_FUNCTION = "easing_function";

    static void registerAll() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> commandDispatcher.register(CommandManager
                .literal("geode_test")
                .then(CommandManager
                        .literal("suggestion_providers")
                        .then(CommandManager.literal("easing_function").then(CommandManager
                                .argument(EASING_FUNCTION, StringArgumentType.word())
                                .suggests(SuggestionProviders.cast(DefaultSuggestionProviders.STANDARD_EASING_FUNCTION))
                                .executes(ModCommands::testSuggestionProviders)
                        ))
                        .then(CommandManager.literal("text_builder")
                                .then(CommandManager.literal("basic").executes(ModCommands::testGTextBasic)))
                        .then(CommandManager.literal("get_human_readable_registry").executes(ModCommands::getHumanReadableRegistry))
                ))
        );
    }

    private static <T> Optional<String> getHumanReadableName(World world, @NonNull T value, @NonNull RegistryKey<Registry<T>> registryKey) {
        return Optional.ofNullable(RegistryHelper.getIdentifierOf(world, registryKey, value)).map(StringHelper::toHumanReadableName);
    }

    static int getHumanReadableRegistry(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        ServerWorld world = source.getWorld();
        String itemName = getHumanReadableName(world, ModItems.ITEM_IDENTIFIER, RegistryKeys.ITEM).orElse("Unknown Item!");
        String groupName = getHumanReadableName(world, ModItemGroups.EXAMPLE_MOD_GROUP.itemGroup(), RegistryKeys.ITEM_GROUP).orElse("Unknown Group!");
        source.sendMessage(TextBuilder.of().text("Item Name: ").text("\"" + itemName + "\"").formatting(Formatting.GREEN).build());
        source.sendMessage(TextBuilder.of().text("Group Name: ").text("\"" + groupName + "\"").formatting(Formatting.GREEN).build());

        return 1;
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
