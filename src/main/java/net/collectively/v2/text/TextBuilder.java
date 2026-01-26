package net.collectively.v2.text;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/// ItemGroupBuilder pattern for creating complicated [texts][Text] in a simple manner.
@SuppressWarnings("unused")
public class TextBuilder {
    // region Static

    /// The [style][Style] used by default for newly created [entries][Entry] when using the [default `of()` method][#of()].
    /// Can be changed when creating many `TextBuilder` in a row to make your life easier.
    /// @see #resetDefaultStyle()
    public static Style DEFAULT_STYLE = Style.EMPTY;

    /// Resets the [global default style][#DEFAULT_STYLE] to the base value ([Style#EMPTY]).
    public static void resetDefaultStyle() {
        DEFAULT_STYLE = Style.EMPTY;
    }

    /// Creates a new `TextBuilder` using the given [default style][Style] as the default style for newly added entries.
    public static TextBuilder of(Style defaultStyle) {
        return new TextBuilder(defaultStyle);
    }

    /// Creates a new `TextBuilder` using the [global default style][#DEFAULT_STYLE] as the default style for newly added entries.
    public static TextBuilder of() {
        return new TextBuilder(DEFAULT_STYLE);
    }

    // endregion

    // region Basic Definition

    /// Contains every [entry][Entry] in this text.
    private final List<Entry> entries = new ArrayList<>();
    /// Represents the default style for newly added entries.
    private final Style defaultStyle;

    private TextBuilder(Style defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    /// Creates a new [mutable text][MutableText] using the texts added to this `TextBuilder`.
    public MutableText build() {
        MutableText current = Text.empty();

        for (Entry entry : entries) {
            current.append(entry.build());
        }

        return current;
    }

    // endregion

    // region Internal

    /// Enqueues a new [entry][Entry] in this text's entries.
    private TextBuilder addEntry(Entry entry) {
        entries.add(entry);
        return this;
    }

    /// Represents a sub text in this [TextBuilder].
    private static class Entry {
        /// The text of this entry.
        public final MutableText text;
        /// The style to apply on this [entry's text][#text].
        public Style style;

        public Entry(MutableText text, Style style) {
            this.text = text;
            this.style = style;
        }

        /// Modifies this entry's [style][#style] using the [unary operator][UnaryOperator] of type [Style].
        public void modify(UnaryOperator<Style> modifier) {
            style = modifier.apply(style);
        }

        /// Builds this entry by applying its [style][#style] on its [text][#text].
        public MutableText build() {
            return text.setStyle(style);
        }
    }

    // endregion

    // region Enqueue

    /// Enqueues a new [text][Text] with the given [style][Style] at the end of the current text chain.
    /// @param text The text to enqueue.
    /// @param style The style of that text.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(Text)
    public TextBuilder text(Text text, Style style) {
        return addEntry(new Entry((MutableText) text, style));
    }

    /// Enqueues a new [literal][Text#literal(String)] `String` text with the given [style][Style] at the end of the current text chain.
    /// @param text The text to enqueue.
    /// @param style The style of that text.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(String)
    public TextBuilder text(String text, Style style) {
        return text(Text.literal(text), style);
    }

    /// Enqueues a new text with the given [style][Style] at the end of the current text chain.
    /// This text is an object which will be converted to a `String` using [String#valueOf(Object)].
    /// @param object The object to enqueue as text.
    /// @param style The style of that text.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(Object)
    public TextBuilder text(Object object, Style style) {
        return text(String.valueOf(object), style);
    }

    /// Enqueues a new [text][Text] at the end of the current text chain.
    /// The [style][Style] applied to this text is this `TextBuilder`'s [default style][#defaultStyle].
    /// @param text The text to enqueue.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(Text, Style)
    public TextBuilder text(Text text) {
        return text(text, defaultStyle);
    }

    /// Enqueues a new [literal][Text#literal(String)] `String` text at the end of the current text chain.
    /// The [style][Style] applied to this text is this `TextBuilder`'s [default style][#defaultStyle].
    /// @param text The text to enqueue.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(String, Style)
    public TextBuilder text(String text) {
        return text(text, defaultStyle);
    }

    /// Enqueues a new text at the end of the current text chain.
    /// This text is an object which will be converted to a `String` using [String#valueOf(Object)].
    /// The [style][Style] applied to this text is this `TextBuilder`'s [default style][#defaultStyle].
    /// @param object The object to enqueue as text.
    /// @return This `TextBuilder`, for chaining.
    /// @see #text(Object, Style)
    public TextBuilder text(Object object) {
        return text(object, defaultStyle);
    }

    // endregion

    // region Style

    /// Sets the [style][Style] of the last added text entry to the given one.
    /// @param style The new style to use.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder style(Style style) {
        entries.getLast().modify(ignored -> style);
        return this;
    }

    /// Modifies the [style][Style] of the last added text entry using the [unary operator][UnaryOperator].
    /// This `TextBuilder` provides the style of the last text to the `UnaryOperator` and sets the style of that text to the returned value.
    /// @param modifier Function taking in the current style of the last style and returning the style to use.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder style(UnaryOperator<Style> modifier) {
        entries.getLast().modify(modifier);
        return this;
    }

    /// Applies the given [formatting][Formatting] on the last added text entry.
    /// @param formatting The formatting to apply on the text.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder formatting(Formatting formatting) {
        entries.getLast().modify(style -> style.withFormatting(formatting));
        return this;
    }

    /// Sets the [color][Style#color] of the style of the last added text entry to the given `Integer` hexadecimal code.
    /// @apiNote There is no need to provide the transparency in the given color code. For example, `0xFFFFFFFF` can be simplified to `0xFFFFFF`.
    /// @param color The hexadecimal `Integer` color.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder color(int color) {
        return style(style -> style.withColor(color));
    }

    /// Sets the [color of the shadow][Style#shadowColor] of the style of the last added text entry to the given `Integer` hexadecimal code.
    /// @apiNote Unlike the [color setter][#color(int)], you will need to also provide the transparency of the shadow color.
    ///          For example, `0xFFFFFF` will always show a fully transparent shadow. In order to have a fully white shadow,
    ///          use `0xffFFFFFF` where the first hexadecimal pair represent the transparency.
    /// @param color The hexadecimal `Integer` color.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder shadowColor(int color) {
        return style(style -> style.withShadowColor(color));
    }

    /// Sets whether the last added text entry should be [bold][Style#bold] or not.
    /// @param bold A `Boolean` representing whether the text should be bold or not.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder bold(boolean bold) {
        return style(style -> style.withBold(bold));
    }

    /// Sets whether the last added text entry should be [italic][Style#italic] or not.
    /// @param italic A `Boolean` representing whether the text should be italic or not.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder italic(boolean italic) {
        return style(style -> style.withItalic(italic));
    }

    /// Sets whether the last added text entry should be [underlined][Style#underlined] or not.
    /// @param underlined A `Boolean` representing whether the text should be underlined or not.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder underline(boolean underlined) {
        return style(style -> style.withUnderline(underlined));
    }

    /// Sets whether the last added text entry should be [strikethrough][Style#strikethrough] or not.
    /// @param strikethrough A `Boolean` representing whether the text should be strikethrough or not.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder strikethrough(boolean strikethrough) {
        return style(style -> style.withStrikethrough(strikethrough));
    }

    /// Sets whether the last added text entry should be [obfuscated][Style#obfuscated] or not.
    /// @param obfuscated A `Boolean` representing whether the text should be obfuscated or not.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder obfuscated(boolean obfuscated) {
        return style(style -> style.withObfuscated(obfuscated));
    }

    /// Sets an [event called upon clicking][ClickEvent] the last added text entry.
    /// @param clickEvent The `event` called when the text segment is clicked.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder clickEvent(ClickEvent clickEvent) {
        return style(style -> style.withClickEvent(clickEvent));
    }

    /// Sets an [event called upon hovering][HoverEvent] the last added text entry.
    /// @param hoverEvent The `event` called when the text segment is hovered.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder hoverEvent(HoverEvent hoverEvent) {
        return style(style -> style.withHoverEvent(hoverEvent));
    }

    /// Sets the [font][Style#font] of the style of the last added text entry.
    /// @param font The font to be used by the text.
    /// @return This `TextBuilder`, for chaining.
    public TextBuilder font(StyleSpriteSource font) {
        return style(style -> style.withFont(font));
    }

    // endregion
}
