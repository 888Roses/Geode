package net.collectively.v2.registration;

import net.collectively.v2.math.math;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public interface ClientRegisterer extends CommonRegisterer {
    @Override
    default void register() {
    }

    default <T> Option<T> registerOption(String identifier, OptionScreen screen, SimpleOption<T> option) {
        Option<T> createdOption = new Option<>(identifier, option, screen);
        ClientCustomRegistries.OPTIONS.add(createdOption);
        return createdOption;
    }

    default Option<Boolean> registerBooleanOption(String identifier, OptionScreen screen, boolean defaultValue) {
        return registerOption(identifier, screen, SimpleOption.ofBoolean(Option.getTranslationKey(id(identifier)), defaultValue));
    }

    default Option<Double> registerPercentageSliderOption(String identifier, OptionScreen screen, double defaultValue) {
        return registerOption(identifier, screen, new SimpleOption<>(
                Option.getTranslationKey(id(identifier)), SimpleOption.emptyTooltip(),
                (text, value) -> Text.translatable("options.percent_value", text, (int) (value * 100d)),
                SimpleOption.DoubleSliderCallbacks.INSTANCE, defaultValue,
                value -> {
                }
        ));
    }

    default Option<Double> registerSliderOption(String identifier, OptionScreen screen, double defaultValue, double min, double max) {
        return registerOption(identifier, screen, new SimpleOption<>(
                Option.getTranslationKey(id(identifier)), SimpleOption.emptyTooltip(),
                (prefix, value) -> Text.translatable(
                        "options.slider_value",
                        prefix,
                        math.round(math.lerp(value, min, max) * 10f) / 10f
                ),
                SimpleOption.DoubleSliderCallbacks.INSTANCE, defaultValue,
                value -> {
                }
        ));
    }

    default Option<Double> registerSlider(String identifier,
                                          OptionScreen screen,
                                          SimpleOption.TooltipFactory<Double> tooltipFactory,
                                          SimpleOption.ValueTextGetter<Double> valueTextGetter,
                                          double defaultValue,
                                          Consumer<Double> callback) {
        return registerOption(identifier, screen, new SimpleOption<>(
                Option.getTranslationKey(id(identifier)), tooltipFactory,
                valueTextGetter, SimpleOption.DoubleSliderCallbacks.INSTANCE, defaultValue, callback
        ));
    }

    default void registerRemovedOption(String identifier) {
        ClientCustomRegistries.REMOVED_OPTIONS.add(identifier);
    }
}