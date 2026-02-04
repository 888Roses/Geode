package net.collectively.geode.datagen;

import net.collectively.geode.helpers.RegistryHelper;
import net.collectively.geode.helpers.StringHelper;
import net.collectively.geode.registration.GeodeEnchantment;
import net.collectively.geode.registration.GeodeItemGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"SameParameterValue", "unused", "UnusedReturnValue"})
public abstract class GeodeDataGeneration implements DataProvider {
    // region Resources

    /// A list of every [runnable][DataGenRunnable] registered in this centralized data generation instance. Those runnable
    /// will eventually generate their DataGen using [DataGenRunnable#run(RegistryWrapper.WrapperLookup,DataGen)].
    private final List<DataGenRunnable<?>> registeredRunnable = new ArrayList<>();

    /// Registers a new [runnable][DataGenRunnable] in this data generation instance.
    protected final <T extends DataGenRunnable<?>> T addRunnable(T runnable) {
        registeredRunnable.add(runnable);
        //noinspection unchecked
        return (T) registeredRunnable.getLast();
    }

    protected final EnchantmentRunnable addEnchantment(GeodeEnchantment value) {
        return addRunnable(new EnchantmentRunnable(value));
    }

    protected final ItemGroupRunnable addItemGroup(GeodeItemGroup value) {
        return addRunnable(new ItemGroupRunnable(value));
    }

    protected final ItemRunnable addItem(Item value) {
        return addRunnable(new ItemRunnable(value));
    }

    protected final BlockRunnable addBlock(Block value) {
        return addRunnable(new BlockRunnable(value));
    }

    protected final BlockEntityRunnable addBlockEntity(BlockEntity value) {
        return addRunnable(new BlockEntityRunnable(value));
    }

    protected final SoundRunnable addSound(SoundEvent value) {
        return addRunnable(new SoundRunnable(value));
    }

    protected final <U extends Entity> EntityTypeRunnable<U> addEntityType(EntityType<U> value) {
        return addRunnable(new EntityTypeRunnable<>(value));
    }

    /// Bootstrap method called to request every [runnable][DataGenRunnable] to generate. Call any runnable registering
    /// method in here.
    protected abstract void generate();

    // endregion

    // region Internal

    /// The engine actually generating the DataGen.
    private final DataGen dataGen;
    /// Future registries.
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    /// Creates a new instance of this centralized data generation engine.
    public GeodeDataGeneration(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        dataGen = new DataGen(dataOutput, registriesFuture);
        this.registriesFuture = registriesFuture;
    }

    /// Generates the DataGen.
    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose((registries) -> {
            generate();

            for (var target : registeredRunnable) {
                target.run(registries, dataGen);
            }

            return CompletableFuture.allOf(
                    dataGen.translations.run(writer),
                    dataGen.enchantments.run(writer),
                    dataGen.enchantmentTags.run(writer),
                    dataGen.models.run(writer)
            );
        });
    }

    @Override
    public String getName() {
        return "GeodeDataGen";
    }

    public abstract String getModId();

    public Identifier linkedModId(String identifier) {
        return Identifier.of(getModId(), identifier);
    }

    // endregion

    // region DataGen

    /// Engine class responsible for generating the actual DataGen.
    /// It contains different providers generating DataGen for different aspects of the game.
    public static class DataGen {
        public final LanguageGenerator translations;
        public final EnchantmentGenerator enchantments;
        public final EnchantmentTagGenerator enchantmentTags;
        public final ModelGenerator models;

        private final Map<String, String> registeredTranslations = new HashMap<>();
        private final Map<Identifier, IncompleteEnchantment> registeredEnchantments = new HashMap<>();
        private final Map<TagKey<Enchantment>, List<Identifier>> registeredEnchantmentTags = new HashMap<>();
        private final List<BlockModelDefinitionCreator> blockStateModelGenerators = new ArrayList<>();

        public DataGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            translations = new LanguageGenerator(dataOutput, registriesFuture);
            enchantments = new EnchantmentGenerator(dataOutput, registriesFuture);
            enchantmentTags = new EnchantmentTagGenerator(dataOutput, registriesFuture);
            models = new ModelGenerator(dataOutput);
        }

        public final class LanguageGenerator extends FabricLanguageProvider {
            public LanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
                super(dataOutput, registryLookup);
            }

            @Override
            public void generateTranslations(RegistryWrapper.@NotNull WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
                registeredTranslations.forEach(translationBuilder::add);
            }

            public void add(String key, String value) {
                registeredTranslations.put(key, value);
            }
        }

        public final class EnchantmentGenerator extends GeodeEnchantmentGenerator {
            public void add(GeodeEnchantment geodeEnchantment, IncompleteEnchantment incompleteEnchantment) {
                registeredEnchantments.put(geodeEnchantment.identifier(), incompleteEnchantment);
            }

            public EnchantmentGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
                super(output, registriesFuture, List.of((registries, exporter) -> {
                    for (var enchantment : registeredEnchantments.entrySet()) {
                        exporter.accept(new EnchantmentEntry(enchantment.getKey(), enchantment.getValue().complete(enchantment.getKey(), registries)));
                    }
                }));
            }
        }

        public final class EnchantmentTagGenerator extends FabricTagProvider<Enchantment> {
            public void add(TagKey<Enchantment> tag, GeodeEnchantment enchantment) {
                if (!registeredEnchantmentTags.containsKey(tag)) {
                    registeredEnchantmentTags.put(tag, new ArrayList<>());
                }

                registeredEnchantmentTags.get(tag).add(enchantment.identifier());
            }

            private EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
                super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
            }

            @Override
            protected void configure(RegistryWrapper.@NotNull WrapperLookup wrapperLookup) {
                registeredEnchantmentTags.forEach((enchantment, identifiers) -> {
                    TagBuilder builder = getTagBuilder(enchantment);
                    identifiers.forEach(builder::addOptional);
                    builder.build();
                });
            }
        }

        public final class ModelGenerator extends FabricModelProvider {
            public void addBlockState(BlockModelDefinitionCreator definitionCreator) {
                blockStateModelGenerators.add(definitionCreator);
            }

            private ModelGenerator(FabricDataOutput output) {
                super(output);
            }

            @Override
            public void generateBlockStateModels(@NonNull BlockStateModelGenerator blockStateModelGenerator) {
                for (BlockModelDefinitionCreator definitionCreator : blockStateModelGenerators) {
                    blockStateModelGenerator.blockStateCollector.accept(definitionCreator);
                }
            }

            @Override
            public void generateItemModels(ItemModelGenerator itemModelGenerator) {

            }
        }
    }

    /// Implements methods used to generate Text translations.
    @SuppressWarnings("unused")
    public interface Translatable<T extends DataGenRunnable<?>> {
        /// Generates the translation of this [runnable][DataGenRunnable] in the lang file.
        T translate(String translation);
    }

    /// Specifies that a method can be used to automatically generate translations based on the identifier of the
    /// [runnable][DataGenRunnable].
    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public interface AutoTranslatable<T extends DataGenRunnable<?>> {
        /// Translates this [runnable][DataGenRunnable] in a similar way to a [normal translatable][Translatable] except the
        /// actual translation is created using the identifier of this runnable.
        /// @see StringHelper#toHumanReadableName(Identifier)
        T autoTranslate();

        /// Creates a `String` human-readable name for the [given value][T] in the given registry referenced by the [RegistryKey].
        /// @param value The registered value we want to get the name of.
        /// @param registryKey Registry key referencing the [Registry] of type [T] in which the value is registered.
        /// @return An [optional][Optional] of `String` containing the human-readable name, or nothing if the value was not
        /// registered.
        /// @see RegistryHelper#getIdentifierOf(RegistryWrapper.WrapperLookup, RegistryKey, Object)
        default <U> Optional<String> getHumanReadableName(RegistryWrapper.@NotNull WrapperLookup registries,
                                                          @NotNull U value,
                                                          @NotNull RegistryKey<Registry<U>> registryKey) {
            return Optional.ofNullable(RegistryHelper.getIdentifierOf(registries, registryKey, value))
                    .map(StringHelper::toHumanReadableName);
        }
    }

    // endregion

    // region Runnable

    /// Represents a [runnable][DataGenRunnable] generating the DataGen of an [enchantment][GeodeEnchantment].
    @SuppressWarnings("unused")
    public static final class EnchantmentRunnable extends DataGenRunnable<GeodeEnchantment> implements
            Translatable<EnchantmentRunnable>,
            AutoTranslatable<EnchantmentRunnable> {

        // region Essentials

        public EnchantmentRunnable(GeodeEnchantment target) {
            super(target);
        }

        @Override
        protected void run(RegistryWrapper.@NotNull WrapperLookup registries, DataGen dataGen) {
            // Generate the translations for the name and description of the enchantment if it isn't null.
            String nameKey = Util.createTranslationKey("enchantment", value.identifier());
            dataGen.translations.add(nameKey, nameTranslation.complete(registries));
            if (descriptionTranslation != null && !descriptionTranslation.isBlank()) {
                dataGen.translations.add(nameKey + ".desc", descriptionTranslation);
            }

            // Generate the enchantment itself.
            if (enchantment != null) {
                dataGen.enchantments.add(value, enchantment);
                // Add to the correct tags for them to appear in the enchanting table.
                if (enchantment.definition.isTreasure) dataGen.enchantmentTags.add(EnchantmentTags.TREASURE, value);
                else dataGen.enchantmentTags.add(EnchantmentTags.NON_TREASURE, value);
            }

            // Generate general tags.
            if (!tags.isEmpty()) {
                for (TagKey<Enchantment> tag : tags) {
                    // Extra safety since treasure or non treasure is determined by the enchantment definition.
                    if (tag == EnchantmentTags.NON_TREASURE || tag == EnchantmentTags.TREASURE) {
                        continue;
                    }

                    dataGen.enchantmentTags.add(tag, value);
                }
            }
        }

        // endregion

        // region Translate

        /// The translation of the name of the enchantment. Cannot be null.
        private IncompleteGetter<String> nameTranslation;
        /// The optional translation of the description of the enchantment. Can be null or empty.
        private String descriptionTranslation;

        @Override
        public EnchantmentRunnable translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public EnchantmentRunnable autoTranslate() {
            nameTranslation = registries -> StringHelper.toHumanReadableName(value.registryKey().getValue());
            return this;
        }

        /// Creates a translation for the description of this enchantment. The description of an enchantment isn't natively
        /// supported; users will require a mod like
        /// [Enchantment Descriptions by Darkhax](https://modrinth.com/mod/enchantment-descriptions) to be able to see them.
        /// While this is optional, it is good practice to include it.
        public EnchantmentRunnable translateDescription(String translation) {
            descriptionTranslation = translation;
            return this;
        }

        // endregion

        // region Enchantment

        /// The enchantment definition of this runnable.
        private IncompleteEnchantment enchantment;

        /// Creates the definition of this enchantment. This is what generates the actual enchantment file located under
        /// `data > enchantments`.
        /// @param supportedItems A tag of items on which the enchantment can be applied.
        /// @return A builder pattern defining the enchantment structure. Call [IncompleteEnchantment#build()] to get back
        /// to this structure.
        public IncompleteEnchantment enchantment(TagKey<Item> supportedItems) {
            return new IncompleteEnchantment(this, RegistrationUtils.itemTag(supportedItems));
        }

        // endregion

        // region Tags

        /// List of tags for this enchantment.
        private final List<TagKey<Enchantment>> tags = new ArrayList<>();

        /// Adds this enchantment to the given tag.
        public EnchantmentRunnable tag(TagKey<Enchantment> tag) {
            tags.add(tag);
            return this;
        }

        // endregion
    }

    public static final class ItemGroupRunnable extends DataGenRunnable<GeodeItemGroup> implements
            Translatable<ItemGroupRunnable>,
            AutoTranslatable<ItemGroupRunnable> {

        // region Essentials

        public ItemGroupRunnable(GeodeItemGroup value) {
            super(value);
        }

        @Override
        protected void run(RegistryWrapper.@NotNull WrapperLookup registries, DataGen dataGen) {
            dataGen.translations.add(value.getTranslationKey(), nameTranslation.complete(registries));
        }

        // endregion

        // region Translate

        /// The translation of the name of the item group. Cannot be null.
        private IncompleteGetter<String> nameTranslation;

        @Override
        public ItemGroupRunnable translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public ItemGroupRunnable autoTranslate() {
            nameTranslation = registries -> StringHelper.toHumanReadableName(value.registryKey().getValue());
            return this;
        }

        // endregion
    }

    public static final class ItemRunnable extends DataGenRunnable<Item> implements
            Translatable<ItemRunnable>,
            AutoTranslatable<ItemRunnable> {
        // region Essentials

        public ItemRunnable(Item item) {
            super(item);
        }

        @Override
        protected void run(@NotNull RegistryWrapper.WrapperLookup registries, DataGen dataGen) {
            dataGen.translations.add(value.getTranslationKey(), nameTranslation.complete(registries));
        }

        // endregion

        // region Translation

        private IncompleteGetter<String> nameTranslation;

        @Override
        public ItemRunnable translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public ItemRunnable autoTranslate() {
            nameTranslation = registries ->
                    Optional.ofNullable(RegistryHelper.getIdentifierOf(registries, RegistryKeys.ITEM, value))
                            .map(StringHelper::toHumanReadableName)
                            .orElse(value.getTranslationKey());

            return this;
        }

        // endregion
    }

    public static final class BlockEntityRunnable extends DataGenRunnable<BlockEntity> {
        // region Essentials

        public BlockEntityRunnable(BlockEntity blockEntity) {
            super(blockEntity);
        }

        @Override
        protected void run(@NotNull RegistryWrapper.WrapperLookup registries, DataGen dataGen) {
        }

        // endregion
    }

    public static final class SoundRunnable extends DataGenRunnable<SoundEvent> {
        // region Essentials

        public SoundRunnable(SoundEvent sound) {
            super(sound);
        }

        @Override
        protected void run(@NotNull RegistryWrapper.WrapperLookup registries, DataGen dataGen) {
            String key = Util.createTranslationKey("subtitle", value.id());
            dataGen.translations.add(key, Optional.ofNullable(subtitleTranslation.complete(registries)).orElse(key));
        }

        // endregion

        // region Translation

        private IncompleteGetter<String> subtitleTranslation = null;

        public SoundRunnable subtitle(String translation) {
            subtitleTranslation = registries -> translation;
            return this;
        }

        // endregion
    }

    public static final class EntityTypeRunnable<U extends Entity> extends DataGenRunnable<EntityType<U>> implements
            Translatable<EntityTypeRunnable<U>>,
            AutoTranslatable<EntityTypeRunnable<U>> {
        // region Essentials

        public EntityTypeRunnable(EntityType<U> entityType) {
            super(entityType);
        }

        @Override
        protected void run(@NotNull RegistryWrapper.WrapperLookup registries, DataGen dataGen) {
            dataGen.translations.add(value.getTranslationKey(), nameTranslation.complete(registries));
        }

        // endregion

        // region Translation

        private IncompleteGetter<String> nameTranslation;

        @Override
        public EntityTypeRunnable<U> translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public EntityTypeRunnable<U> autoTranslate() {
            nameTranslation = registries ->
                    Optional.ofNullable(RegistryHelper.getIdentifierOf(registries, RegistryKeys.ENTITY_TYPE, value))
                            .map(StringHelper::toHumanReadableName)
                            .orElse(value.getTranslationKey());

            return this;
        }

        // endregion
    }

    public final class BlockRunnable extends DataGenRunnable<Block> implements
            Translatable<BlockRunnable>,
            AutoTranslatable<BlockRunnable> {
        // region Essentials

        public BlockRunnable(Block block) {
            super(block);
        }

        @Override
        protected void run(@NotNull RegistryWrapper.WrapperLookup registries, DataGen dataGen) {
            dataGen.translations.add(value.getTranslationKey(), nameTranslation.complete(registries));
            if (blockStateDefinitionCreator != null) dataGen.models.addBlockState(blockStateDefinitionCreator);
        }

        // endregion

        // region Translation

        private IncompleteGetter<String> nameTranslation;

        @Override
        public BlockRunnable translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public BlockRunnable autoTranslate() {
            nameTranslation = registries ->
                    Optional.ofNullable(RegistryHelper.getIdentifierOf(registries, RegistryKeys.BLOCK, value))
                            .map(StringHelper::toHumanReadableName)
                            .orElse(value.getTranslationKey());

            return this;
        }

        // endregion

        // region Block State

        private BlockModelDefinitionCreator blockStateDefinitionCreator;

        public BlockRunnable variantsBlockstate(Function<VariantsBlockModelDefinitionCreator.Empty, VariantsBlockModelDefinitionCreator> definition) {
            blockStateDefinitionCreator = definition.apply(VariantsBlockModelDefinitionCreator.of(getValue()));
            return this;
        }

        public BlockRunnable multipartBlockstate(UnaryOperator<MultipartDefinitionCreator> definition) {
            blockStateDefinitionCreator = definition.apply(new MultipartDefinitionCreator(getValue()));
            return this;
        }

        // endregion
    }

    // endregion

    // region Util

    /// Collection of utilities regarding registration of [runnable][DataGenRunnable].
    @SuppressWarnings("unused")
    public interface RegistrationUtils {
        /// Retrieves the [registry][Registry] at the given [registry key][RegistryKey] using the
        /// [given registries][net.minecraft.registry.RegistryWrapper.WrapperLookup].
        /// @param registriesFuture The wrapper lookup to retrieve the registry from.
        /// @param registryKey The [key][RegistryKey] of this registry.
        /// @return The retrieved registry.
        /// @see IncompleteGetter
        static <T> RegistryWrapper.Impl<T> getRegistry(RegistryWrapper.@NotNull WrapperLookup registriesFuture, RegistryKey<? extends Registry<T>> registryKey) {
            return registriesFuture.getOrThrow(registryKey);
        }

        /// Retrieves a [registry entry list][RegistryEntryList] containing every [item][Item] in the given [tag][TagKey].
        /// @param tag The tag we want to get the contents of.
        /// @return An [incomplete getter][IncompleteGetter] containing the list of item. This incomplete getter will be
        /// [complete][IncompleteGetter#complete(net.minecraft.registry.RegistryWrapper.WrapperLookup)] when generating
        /// DataGen.
        static IncompleteGetter<RegistryEntryList<Item>> itemTag(TagKey<Item> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ITEM).getOrThrow(tag);
        }

        /// Retrieves a [registry entry list][RegistryEntryList] containing every [damage type][DamageType] in the given [tag][TagKey].
        /// @param tag The tag we want to get the contents of.
        /// @return An [incomplete getter][IncompleteGetter] containing the list of damage. This incomplete getter will be
        /// [complete][IncompleteGetter#complete(net.minecraft.registry.RegistryWrapper.WrapperLookup)] when generating
        /// DataGen.
        static IncompleteGetter<RegistryEntryList<DamageType>> damageTag(TagKey<DamageType> tag) {
            return registries -> getRegistry(registries, RegistryKeys.DAMAGE_TYPE).getOrThrow(tag);
        }

        /// Retrieves a [registry entry list][RegistryEntryList] containing every [enchantment][Enchantment] in the given [tag][TagKey].
        /// @param tag The tag we want to get the contents of.
        /// @return An [incomplete getter][IncompleteGetter] containing the list of enchantment. This incomplete getter will
        /// be [complete][IncompleteGetter#complete(net.minecraft.registry.RegistryWrapper.WrapperLookup)] when generating
        /// DataGen.
        static IncompleteGetter<RegistryEntryList<Enchantment>> enchantmentTag(TagKey<Enchantment> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ENCHANTMENT).getOrThrow(tag);
        }

        /// Retrieves a [registry entry list][RegistryEntryList] containing every [block][Block] in the given [tag][TagKey].
        /// @param tag The tag we want to get the contents of.
        /// @return An [incomplete getter][IncompleteGetter] containing the list of block. This incomplete getter will be
        /// [complete][IncompleteGetter#complete(net.minecraft.registry.RegistryWrapper.WrapperLookup)] when generating
        /// DataGen.
        static IncompleteGetter<RegistryEntryList<Block>> blockTag(TagKey<Block> tag) {
            return registries -> getRegistry(registries, RegistryKeys.BLOCK).getOrThrow(tag);
        }

        /// Retrieves a [registry entry list][RegistryEntryList] containing every [entity type][EntityType] in the given [tag][TagKey].
        /// @param tag The tag we want to get the contents of.
        /// @return An [incomplete getter][IncompleteGetter] containing the list of entity type. This incomplete getter will
        /// be [complete][IncompleteGetter#complete(net.minecraft.registry.RegistryWrapper.WrapperLookup)] when generating
        /// DataGen.
        static IncompleteGetter<RegistryEntryList<EntityType<?>>> entityTypeTag(TagKey<EntityType<?>> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ENTITY_TYPE).getOrThrow(tag);
        }
    }

    // endregion

    // region Structures

    /// Builder pattern defining an [enchantment][Enchantment] for which we want to generate DataGen. Remember to always call
    /// [IncompleteEnchantment#build()] to register it and go back to the parent [runnable][DataGenRunnable].
    @SuppressWarnings("unused")
    public static final class IncompleteEnchantment {
        private final EnchantmentRunnable parent;
        private final IncompleteDefinition definition;
        private IncompleteGetter<RegistryEntryList<Enchantment>> exclusiveSet = lookup -> RegistryEntryList.of();
        private final Map<ComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final ComponentMap.Builder effectMap = ComponentMap.builder();

        private IncompleteEnchantment(EnchantmentRunnable parent, IncompleteGetter<RegistryEntryList<Item>> supportedItems) {
            this.parent = parent;
            this.definition = new IncompleteDefinition(supportedItems);
        }

        /// Registers this enchantment to be generated in DataGen.
        public EnchantmentRunnable build() {
            parent.enchantment = this;
            return this.parent;
        }

        /// A [tag][TagKey] of [enchantments][Enchantment] representing every enchantment incompatible with this one.
        public IncompleteEnchantment exclusiveSet(TagKey<Enchantment> exclusiveSet) {
            this.exclusiveSet = RegistrationUtils.enchantmentTag(exclusiveSet);
            return this;
        }

        public <E> @NotNull IncompleteEnchantment addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect, LootCondition.Builder requirements) {
            getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NotNull IncompleteEnchantment addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect) {
            this.getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.empty()));
            return this;
        }

        public <E> @NotNull IncompleteEnchantment addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect, LootCondition.Builder requirements) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NotNull IncompleteEnchantment addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.empty()));
            return this;
        }

        public @NotNull GeodeDataGeneration.IncompleteEnchantment addEffect(ComponentType<List<AttributeEnchantmentEffect>> type, AttributeEnchantmentEffect effect) {
            this.getEffectsList(type).add(effect);
            return this;
        }

        public <E> @NotNull IncompleteEnchantment addNonListEffect(ComponentType<E> type, E effect) {
            this.effectMap.add(type, effect);
            return this;
        }

        public @NotNull GeodeDataGeneration.IncompleteEnchantment addEffect(ComponentType<Unit> type) {
            this.effectMap.add(type, Unit.INSTANCE);
            return this;
        }

        @SuppressWarnings("unchecked")
        private <E> List<E> getEffectsList(ComponentType<List<E>> type) {
            return (List<E>) this.effectLists.computeIfAbsent(type, ignored -> {
                ArrayList<E> arrayList = new ArrayList<>();
                this.effectMap.add(type, arrayList);
                return arrayList;
            });
        }

        public IncompleteEnchantment primaryItems(TagKey<Item> items) {
            definition.primaryItems = Optional.ofNullable(items == null ? null : RegistrationUtils.itemTag(items));
            return this;
        }

        public IncompleteEnchantment weight(int weight) {
            definition.weight = weight;
            return this;
        }

        public IncompleteEnchantment maxLevel(int maxLevel) {
            definition.maxLevel = maxLevel;
            return this;
        }

        public IncompleteEnchantment minCost(int base, int perLevelAboveFirst) {
            definition.minCost = new Enchantment.Cost(base, perLevelAboveFirst);
            return this;
        }

        public IncompleteEnchantment maxCost(int base, int perLevelAboveFirst) {
            definition.maxCost = new Enchantment.Cost(base, perLevelAboveFirst);
            return this;
        }

        public IncompleteEnchantment anvilCost(int anvilCost) {
            definition.anvilCost = anvilCost;
            return this;
        }

        public IncompleteEnchantment isTreasure(boolean isTreasure) {
            definition.isTreasure = isTreasure;
            return this;
        }

        public IncompleteEnchantment addSlot(AttributeModifierSlot slot) {
            if (!definition.slots.contains(slot)) definition.slots.add(slot);
            return this;
        }

        public Enchantment complete(Identifier identifier, RegistryWrapper.WrapperLookup registries) {
            return new Enchantment(
                    Text.translatable(Util.createTranslationKey("enchantment", identifier)),
                    definition.complete(identifier, registries),
                    exclusiveSet.complete(registries),
                    effectMap.build()
            );
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public static class IncompleteDefinition {
            public final List<AttributeModifierSlot> slots = new ArrayList<>();
            public final IncompleteGetter<RegistryEntryList<Item>> supportedItems;
            public Optional<IncompleteGetter<RegistryEntryList<Item>>> primaryItems = Optional.empty();
            public int weight = 1;
            public int maxLevel = 1;
            public Enchantment.Cost minCost = new Enchantment.Cost(1, 1);
            public Enchantment.Cost maxCost = new Enchantment.Cost(1, 1);
            public int anvilCost = 1;
            public boolean isTreasure;

            private IncompleteDefinition(IncompleteGetter<RegistryEntryList<Item>> supportedItems) {
                this.supportedItems = supportedItems;
            }

            public Enchantment.Definition complete(Identifier identifier, RegistryWrapper.WrapperLookup registries) {
                return new Enchantment.Definition(
                        supportedItems.complete(registries),
                        primaryItems.map(x -> x.complete(registries)),
                        weight,
                        maxLevel,
                        minCost,
                        maxCost,
                        anvilCost,
                        slots
                );
            }
        }
    }

    public class MultipartDefinitionCreator implements BlockModelDefinitionCreator {
        private final List<Part> parts = new ArrayList<>();
        private final Block block;

        public MultipartDefinitionCreator(Block block) {
            this.block = block;
        }

        public Block getBlock() {
            return this.block;
        }


        // region with

        public MultipartDefinitionCreator with(@Nullable MultipartModelCondition condition, WeightedVariant variant) {
            if (condition != null) {
                validate(condition);
            }

            Part part = new Part(Optional.ofNullable(condition), variant);
            parts.add(part);

            return this;
        }

        // region utilities

        // region condition

        public MultipartDefinitionCreator with(@Nullable MultipartModelCondition condition,
                                               UnaryOperator<ModelVariant> operator,
                                               Identifier... variants) {
            return with(
                    condition,
                    // "Bakes" all the model variants into a single weighted variant
                    BlockStateModelGenerator.createWeightedVariant(Arrays.stream(variants)
                            // Turns the identifier into a model variant
                            .map(BlockStateModelGenerator::createModelVariant)
                            // Applies the operator on the created model variant
                            .map(operator)
                            // Creates an array from the stream
                            .toArray(ModelVariant[]::new)
                    )
            );
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelCondition condition,
                                               UnaryOperator<ModelVariant> operator,
                                               String... variants) {
            return with(
                    condition,
                    operator,
                    Arrays.stream(variants).map(GeodeDataGeneration.this::linkedModId).toArray(Identifier[]::new)
            );
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelCondition condition, Identifier... variants) {
            return with(condition, ignored -> ignored, variants);
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelCondition condition, String... variants) {
            return with(condition, ignored -> ignored, variants);
        }

        // endregion

        // region condition builder

        public MultipartDefinitionCreator with(@Nullable MultipartModelConditionBuilder condition,
                                               UnaryOperator<ModelVariant> operator,
                                               Identifier... variants) {
            return with(
                    Optional.ofNullable(condition).map(MultipartModelConditionBuilder::build).orElse(null),
                    operator,
                    variants
            );
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelConditionBuilder condition,
                                               UnaryOperator<ModelVariant> operator,
                                               String... variants) {
            return with(
                    Optional.ofNullable(condition).map(MultipartModelConditionBuilder::build).orElse(null),
                    operator,
                    variants
            );
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelConditionBuilder condition, Identifier... variants) {
            return with(
                    Optional.ofNullable(condition).map(MultipartModelConditionBuilder::build).orElse(null),
                    ignored -> ignored,
                    variants
            );
        }

        public MultipartDefinitionCreator with(@Nullable MultipartModelConditionBuilder condition, String... variants) {
            return with(
                    Optional.ofNullable(condition).map(MultipartModelConditionBuilder::build).orElse(null),
                    ignored -> ignored,
                    variants
            );
        }

        // region properties

        // region identifiers

        public <T extends Comparable<T>> MultipartDefinitionCreator with(
                Property<T> property, T value,

                UnaryOperator<ModelVariant> operator,
                Identifier... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>> MultipartDefinitionCreator with(
                Property<T> property, T value,

                Identifier... variants
        ) {
            return with(property, value, ignored -> ignored, variants);
        }

        public <T extends Comparable<T>, U extends Comparable<U>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,

                UnaryOperator<ModelVariant> operator,
                Identifier... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                            .put(property2, value2)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>, U extends Comparable<U>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,

                Identifier... variants
        ) {
            return with(property, value, property2, value2, ignored -> ignored, variants);
        }

        public <T extends Comparable<T>, U extends Comparable<U>, V extends Comparable<V>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,
                Property<V> property3, V value3,

                UnaryOperator<ModelVariant> operator,
                Identifier... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                            .put(property2, value2)
                            .put(property3, value3)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>, U extends Comparable<U>, V extends Comparable<V>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,
                Property<V> property3, V value3,

                Identifier... variants
        ) {
            return with(property, value, property2, value2, property3, value3, ignored -> ignored, variants);
        }

        // endregion

        // region strings

        public <T extends Comparable<T>> MultipartDefinitionCreator with(
                Property<T> property, T value,

                UnaryOperator<ModelVariant> operator,
                String... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>> MultipartDefinitionCreator with(
                Property<T> property, T value,

                String... variants
        ) {
            return with(property, value, ignored -> ignored, variants);
        }

        public <T extends Comparable<T>, U extends Comparable<U>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,

                UnaryOperator<ModelVariant> operator,
                String... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                            .put(property2, value2)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>, U extends Comparable<U>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,

                String... variants
        ) {
            return with(property, value, property2, value2, ignored -> ignored, variants);
        }

        public <T extends Comparable<T>, U extends Comparable<U>, V extends Comparable<V>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,
                Property<V> property3, V value3,

                UnaryOperator<ModelVariant> operator,
                String... variants
        ) {
            return with(
                    BlockStateModelGenerator.createMultipartConditionBuilder()
                            .put(property, value)
                            .put(property2, value2)
                            .put(property3, value3)
                    ,
                    operator,
                    variants
            );
        }

        public <T extends Comparable<T>, U extends Comparable<U>, V extends Comparable<V>> MultipartDefinitionCreator with(
                Property<T> property, T value,
                Property<U> property2, U value2,
                Property<V> property3, V value3,

                String... variants
        ) {
            return with(property, value, property2, value2, property3, value3, ignored -> ignored, variants);
        }

        // endregion

        // endregion

        // endregion

        // region unconditional

        public MultipartDefinitionCreator with(UnaryOperator<ModelVariant> operator, Identifier... variants) {
            return with((MultipartModelCondition) null, operator, variants);
        }

        public MultipartDefinitionCreator with(UnaryOperator<ModelVariant> operator, String... variants) {
            return with((MultipartModelCondition) null, operator, variants);
        }

        public MultipartDefinitionCreator with(Identifier... variants) {
            return with((MultipartModelCondition) null, ignored -> ignored, variants);
        }

        public MultipartDefinitionCreator with(String... variants) {
            return with((MultipartModelCondition) null, ignored -> ignored, variants);
        }

        // endregion

        // endregion

        // endregion

        private void validate(MultipartModelCondition selector) {
            selector.instantiate(this.block.getStateManager());
        }

        public BlockModelDefinition createBlockModelDefinition() {
            return new BlockModelDefinition(Optional.empty(), Optional.of(new BlockModelDefinition.Multipart(this.parts.stream().map(Part::toComponent).toList())));
        }

        @Environment(EnvType.CLIENT)
        private record Part(Optional<MultipartModelCondition> condition, WeightedVariant variants) {
            public MultipartModelComponent toComponent() {
                return new MultipartModelComponent(this.condition, this.variants.toModel());
            }

            public Optional<MultipartModelCondition> condition() {
                return this.condition;
            }

            public WeightedVariant variants() {
                return this.variants;
            }
        }
    }

    // endregion
}