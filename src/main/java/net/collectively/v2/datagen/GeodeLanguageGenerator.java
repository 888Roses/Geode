package net.collectively.v2.datagen;

import net.collectively.v2.helpers.RegistryHelper;
import net.collectively.v2.helpers.StringHelper;
import net.collectively.v2.registration.GeodeEnchantment;
import net.collectively.v2.registration.GeodeGroup;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class GeodeLanguageGenerator extends FabricLanguageProvider {
    private static final String ENCHANTMENT_NOT_FOUND_ERR = "Couldn't find enchantment in enchantment registry! Make sure it is registered properly before running datagen.";

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

    /// Creates the name translation for the given [block][Block].
    /// @see #addBlock(Block)
    protected final void addBlock(Block block, String name) {
        translationBuilder.add(block, name);
    }

    /// Creates the name translation for the given [block][Block]. Its translation is automatically created using its
    /// identifier.
    /// @see #addBlock(Block, String)
    protected final void addBlock(Block block) {
        translationBuilder.add(block, getHumanReadableName(block, RegistryKeys.BLOCK).orElse(block.getTranslationKey()));
    }

    /// Creates the name translation for the given [item][Item].
    /// @see #addItem(Item)
    protected final void addItem(Item item, String name) {
        translationBuilder.add(item, name);
    }

    /// Creates the name translation for the given [item][Item]. Its translation is automatically created using its
    /// identifier.
    /// @see #addItem(Item, String)
    protected final void addItem(Item item) {
        translationBuilder.add(item, getHumanReadableName(item, RegistryKeys.ITEM).orElse(item.getTranslationKey()));
    }

    /// Creates the subtitle translation for the given [sound][SoundEvent].
    /// @see #addSound(SoundEvent)
    protected final void addSound(SoundEvent soundEvent, String subtitle) {
        translationBuilder.add(soundEvent, subtitle);
    }

    /// Creates the subtitle translation for the given [sound][SoundEvent]. Its translation is automatically created using
    /// its identifier.
    /// @see #addSound(SoundEvent, String)
    protected final void addSound(SoundEvent soundEvent) {
        addSound(soundEvent, getHumanReadableName(soundEvent, RegistryKeys.SOUND_EVENT)
                .orElse(Util.createTranslationKey("subtitle", soundEvent.id())));
    }

    /// Creates the name translation for the given [item group][GeodeGroup].
    /// @see #addItemGroup(GeodeGroup)
    protected final void addItemGroup(GeodeGroup itemGroup, String name) {
        translationBuilder.add(itemGroup.getTranslationKey(), name);
    }

    /// Creates the name translation for the given [item group][GeodeGroup]. Its translation is automatically created using
    /// its identifier.
    /// @see #addItemGroup(GeodeGroup, String)
    protected final void addItemGroup(GeodeGroup itemGroup) {
        addItemGroup(itemGroup, getHumanReadableName(itemGroup.itemGroup(), RegistryKeys.ITEM_GROUP).orElse(itemGroup.getTranslationKey()));
    }

    /// Creates the name translation for the given [entity][EntityType].
    /// @see #addEntityType(EntityType)
    protected final void addEntityType(EntityType<?> type, String name) {
        translationBuilder.add(type, name);
    }

    /// Creates the name translation for the given [entity][EntityType]. Its translation is automatically created using its
    /// identifier.
    /// @see #addEntityType(EntityType, String)
    protected final void addEntityType(EntityType<?> type) {
        addEntityType(type, getHumanReadableName(type, RegistryKeys.ENTITY_TYPE).orElse(type.getTranslationKey()));
    }

    /// Creates the name translation for the given [enchantment][GeodeEnchantment].
    /// @see #addEnchantment(GeodeEnchantment)
    protected final void addEnchantment(GeodeEnchantment enchantment, String name) {
        translationBuilder.addEnchantment(enchantment.registryKey(), name);
    }

    /// Creates the name translation for the given [enchantment][GeodeEnchantment]. Its translation is automatically created using
    /// its identifier.
    /// @see #addEnchantment(GeodeEnchantment, String)
    protected final void addEnchantment(GeodeEnchantment enchantment) {
        addEnchantment(enchantment, StringHelper.toHumanReadableName(enchantment.registryKey().getValue()));
    }

    /// Creates the description translation for the given [enchantment][GeodeEnchantment].
    /// Note that this description will only be visible using mods such as [Enchantment Descriptions](https://modrinth.com/mod/enchantment-descriptions).
    /// The generated translation key is the same as any enchantment with the suffix `.desc` (i.e. `enchantment.example_mod.brutal.desc`).
    protected final void addEnchantmentDescription(GeodeEnchantment enchantment, String description) {
        String translationKey = Util.createTranslationKey("enchantment", enchantment.registryKey().getValue()) + ".desc";
        translationBuilder.add(translationKey, description);
    }

    /// Creates the name translation for the given [potion effect][StatusEffect].
    /// @see #addStatusEffect(StatusEffect)
    protected final void addStatusEffect(StatusEffect statusEffect, String name) {
        translationBuilder.add(statusEffect.getTranslationKey(), name);
    }

    /// Creates the name translation for the given [potion effect][StatusEffect]. Its translation is automatically created
    /// using its identifier.
    /// @see #addStatusEffect(StatusEffect, String)
    protected final void addStatusEffect(StatusEffect statusEffect) {
        addStatusEffect(statusEffect, getHumanReadableName(statusEffect, RegistryKeys.STATUS_EFFECT).orElse(statusEffect.getTranslationKey()));
    }

    // endregion
}