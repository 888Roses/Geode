package net.collectively.geode.v2.mixin;

import net.collectively.geode.v2.util.interfaces.DrawContextGetter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(DrawContext.class)
public class DrawContextMixin implements DrawContextGetter {
    @Override
    public void drawTextWithOutline(TextRenderer textRenderer, Text text, int x, int y, int color, int outlineColor, boolean drawCorners) {
        DrawContext ctx = (DrawContext) (Object) this;

        // Outline
        MutableText outlineText = text.copy().withColor(outlineColor);
        ctx.drawText(textRenderer, outlineText, x, y - 1, outlineColor, false);
        ctx.drawText(textRenderer, outlineText, x, y + 1, outlineColor, false);
        ctx.drawText(textRenderer, outlineText, x - 1, y, outlineColor, false);
        ctx.drawText(textRenderer, outlineText, x + 1, y, outlineColor, false);

        if (drawCorners) {
            ctx.drawText(textRenderer, outlineText, x + 1, y + 1, outlineColor, false);
            ctx.drawText(textRenderer, outlineText, x + 1, y - 1, outlineColor, false);
            ctx.drawText(textRenderer, outlineText, x - 1, y + 1, outlineColor, false);
            ctx.drawText(textRenderer, outlineText, x - 1, y - 1, outlineColor, false);
        }

        // Text
        ctx.drawText(textRenderer, text, x, y, color, false);
    }
}
