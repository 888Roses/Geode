package net.collectively.v2.datagen;

import net.collectively.v2.helpers.RegistryHelper;
import net.collectively.v2.helpers.StringHelper;
import net.collectively.v2.registration.GeodeGroup;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class GeodeLanguageGenerator extends FabricLanguageProvider {
    // region Provider

    public GeodeLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public final void generateTranslations(RegistryWrapper.@NonNull WrapperLookup registryLookup, @NonNull TranslationBuilder translationBuilder) {
        this.registryLookup = registryLookup;
        this.translationBuilder = translationBuilder;
        generate();
    }

    // endregion

    protected abstract void generate();

    // region Access

    private RegistryWrapper.WrapperLookup registryLookup;
    private TranslationBuilder translationBuilder;

    protected final RegistryWrapper.@NonNull WrapperLookup getRegistryLookup() {
        return this.registryLookup;
    }

    protected final @NonNull TranslationBuilder getTranslationBuilder() {
        return this.translationBuilder;
    }

    /// Creates a `String` human-readable name for the [given value][T] in the given registry referenced by the [RegistryKey].
    ///
    /// @param <T> The type of the registered value we want to get the name of.
    /// @param value The registered value we want to get the name of.
    /// @param registryKey Registry key referencing the [Registry] of type [T] in which the value is registered.
    ///
    /// @return An [optional][Optional] of `String` containing the human-readable name, or nothing if the value was not registered.
    /// @see RegistryHelper#getIdentifierOf(RegistryWrapper.WrapperLookup, RegistryKey, Object)
    protected final <T> Optional<String> getHumanReadableName(@NonNull T value, @NonNull RegistryKey<Registry<T>> registryKey) {
        return Optional.ofNullable(RegistryHelper.getIdentifierOf(registryLookup, registryKey, value)).map(StringHelper::toHumanReadableName);
    }

    // endregion

    // region Generation

    protected final void addBlock(Block block) {
        translationBuilder.add(block, getHumanReadableName(block, RegistryKeys.BLOCK).orElse(block.getTranslationKey()));
    }
    protected final void addBlock(Block block, String name) {
        translationBuilder.add(block, name);
    }

    protected final void addItem(Item item) {
        translationBuilder.add(item, getHumanReadableName(item, RegistryKeys.ITEM).orElse(item.getTranslationKey()));
    }
    protected final void addItem(Item item, String name) {
        translationBuilder.add(item, name);
    }

    protected final void addSound(SoundEvent soundEvent) {
        addSound(soundEvent, getHumanReadableName(soundEvent, RegistryKeys.SOUND_EVENT)
                .orElse(Util.createTranslationKey("subtitles", soundEvent.id())));
    }
    protected final void addSound(SoundEvent soundEvent, String name) {
        translationBuilder.add(soundEvent, name);
    }

    protected final void addItemGroup(GeodeGroup itemGroup) {
        addItemGroup(itemGroup, getHumanReadableName(itemGroup.itemGroup(), RegistryKeys.ITEM_GROUP).orElse(itemGroup.getTranslationKey()));
    }
    protected final void addItemGroup(GeodeGroup itemGroup, String name) {
        translationBuilder.add(itemGroup.getTranslationKey(), name);
    }

    // endregion
}