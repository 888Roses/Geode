package net.collectively.geode.registration;

import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ItemGroupBuilder {
    private static final Identifier DEFAULT_TEXTURE = ItemGroup.getTabTextureId("items");
    private static final ItemGroup.EntryCollector EMPTY = (displayContext, entries) -> {
    };

    private Text displayName = Text.empty();
    private Supplier<ItemStack> iconSupplier = () -> ItemStack.EMPTY;
    private Identifier texture = DEFAULT_TEXTURE;

    private boolean shouldShowScrollbar = true;
    private boolean shouldRenderName = true;
    private boolean isSpecial = false;

    public static ItemGroupBuilder of() {
        return new ItemGroupBuilder();
    }

    private ItemGroupBuilder() {
    }

    public ItemGroupBuilder withDisplayName(Text displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemGroupBuilder withIcon(Supplier<ItemStack> iconSupplier) {
        this.iconSupplier = iconSupplier;
        return this;
    }

    public ItemGroupBuilder withTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public ItemGroupBuilder withIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
        return this;
    }

    public ItemGroupBuilder shouldRenderName(boolean shouldRenderName) {
        this.shouldRenderName = shouldRenderName;
        return this;
    }

    public ItemGroupBuilder shouldShowScrollbar(boolean shouldShowScrollbar) {
        this.shouldShowScrollbar = shouldShowScrollbar;
        return this;
    }

    public FabricItemGroupBuilderImpl build(Identifier identifier) {
        FabricItemGroupBuilderImpl builder = (FabricItemGroupBuilderImpl) new FabricItemGroupBuilderImpl()
                .displayName(displayName)
                .icon(iconSupplier)
                .entries(EMPTY)
                .texture(texture);

        if (displayName == null) builder.displayName(Text.translatable(GeodeItemGroup.getTranslationKey(identifier)));

        if (isSpecial) builder.special();
        if (!shouldRenderName) builder.noRenderedName();
        if (!shouldShowScrollbar) builder.noScrollbar();

        return builder;
    }
}
