package net.collectively.geode.v2.util.interfaces;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public interface DrawContextGetter {
    void drawTextWithOutline(TextRenderer textRenderer, Text text, int x, int y, int color, int outlineColor, boolean drawCorners);
    default void drawTextWithOutline(TextRenderer textRenderer, String text, int x, int y, int color, int outlineColor, boolean drawCorners) {drawTextWithOutline(textRenderer, Text.literal(text), x, y, color, outlineColor, drawCorners);}
    default void drawTextWithOutline(TextRenderer textRenderer, Text text, int x, int y, int color, int outlineColor) {drawTextWithOutline(textRenderer, text, x, y, color, outlineColor, false);}
    default void drawTextWithOutline(TextRenderer textRenderer, String text, int x, int y, int color, int outlineColor) {drawTextWithOutline(textRenderer, text, x, y, color, outlineColor, false);}
}
