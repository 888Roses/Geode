package net.collectively.v2.datagen;

import net.collectively.v2.helpers.RegistryHelper;
import net.collectively.v2.helpers.StringHelper;
import net.collectively.v2.registration.GeodeEnchantment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class GeodeDataGeneration implements DataProvider {
    // region Resources

    private final List<DataGenTarget<?>> dataGenTargets = new ArrayList<>();

    protected final <T extends DataGenTarget<?>> T addDataGenTarget(T target) {
        dataGenTargets.add(target);
        //noinspection unchecked
        return (T) dataGenTargets.getLast();
    }

    @SuppressWarnings("SameParameterValue")
    protected final EnchantmentData addEnchantment(GeodeEnchantment geodeEnchantment) {
        return addDataGenTarget(new EnchantmentData(geodeEnchantment));
    }

    protected abstract void generate();

    // endregion

    // region Internal

    private final DataGen dataGen;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public GeodeDataGeneration(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        dataGen = new DataGen(dataOutput, registriesFuture);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose((registries) -> {
            generate();

            for (var target : dataGenTargets) {
                target.run(registries, dataGen);
            }

            return CompletableFuture.allOf(
                    dataGen.translations.run(writer),
                    dataGen.enchantments.run(writer),
                    dataGen.enchantmentTags.run(writer)
            );
        });
    }

    @Override
    public String getName() {
        return "GeodeDataGen";
    }

    // endregion

    // region DataGen

    public static class DataGen {
        public final LanguageGenerator translations;
        public final EnchantmentGenerator enchantments;
        public final EnchantmentTagGenerator enchantmentTags;

        private final Map<String, String> registeredTranslations = new HashMap<>();
        private final Map<Identifier, CompletableEnchantment> registeredEnchantments = new HashMap<>();
        private final Map<TagKey<Enchantment>, List<Identifier>> registeredEnchantmentTags = new HashMap<>();

        public DataGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            translations = new LanguageGenerator(dataOutput, registriesFuture);
            enchantments = new EnchantmentGenerator(dataOutput, registriesFuture);
            enchantmentTags = new EnchantmentTagGenerator(dataOutput, registriesFuture);
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
            public void add(GeodeEnchantment geodeEnchantment, CompletableEnchantment completableEnchantment) {
                registeredEnchantments.put(geodeEnchantment.identifier(), completableEnchantment);
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
            protected void configure(RegistryWrapper.@NonNull WrapperLookup wrapperLookup) {
                registeredEnchantmentTags.forEach((enchantment, identifiers) -> {
                    TagBuilder builder = getTagBuilder(enchantment);
                    identifiers.forEach(builder::addOptional);
                    builder.build();
                });
            }
        }
    }

    @SuppressWarnings("unused")
    public abstract static class DataGenTarget<T> {
        protected final T target;

        public DataGenTarget(T target) {
            this.target = target;
        }

        public T getTarget() {
            return target;
        }

        protected abstract void run(RegistryWrapper.@NotNull WrapperLookup registries, DataGen dataGen);
    }

    @SuppressWarnings("unused")
    public interface Translatable<T extends DataGenTarget<?>> {
        T translate(String translation);
    }

    @SuppressWarnings({"unused", "UnusedReturnValue"})
    public interface AutoTranslatable<T extends DataGenTarget<?>> {
        T autoTranslate();

        /// Creates a `String` human-readable name for the [given value][T] in the given registry referenced by the
        /// [RegistryKey].
        ///
        /// @param value
        ///         The registered value we want to get the name of.
        /// @param registryKey
        ///         Registry key referencing the [Registry] of type [T] in which the value is registered.
        /// @return An [optional][Optional] of `String` containing the human-readable name, or nothing if the value was not
        ///                                                                 registered.
        /// @see RegistryHelper#getIdentifierOf(RegistryWrapper.WrapperLookup, RegistryKey, Object)
        default <U> Optional<String> getHumanReadableName(RegistryWrapper.@NotNull WrapperLookup registries,
                                                          @NotNull U value,
                                                          @NotNull RegistryKey<Registry<U>> registryKey) {
            return Optional.ofNullable(RegistryHelper.getIdentifierOf(registries, registryKey, value))
                    .map(StringHelper::toHumanReadableName);
        }
    }

    @SuppressWarnings("unused")
    public static final class EnchantmentData extends DataGenTarget<GeodeEnchantment> implements
            Translatable<EnchantmentData>,
            AutoTranslatable<EnchantmentData> {

        // region Essentials

        public EnchantmentData(GeodeEnchantment target) {
            super(target);
        }

        @Override
        protected void run(RegistryWrapper.@NotNull WrapperLookup registries, DataGen dataGen) {
            // Generate the translations for the name and description of the enchantment if it isn't null.
            String nameKey = Util.createTranslationKey("enchantment", target.identifier());
            dataGen.translations.add(nameKey, nameTranslation.complete(registries));
            if (descriptionTranslation != null && !descriptionTranslation.isBlank()) {
                dataGen.translations.add(nameKey + ".desc", descriptionTranslation);
            }

            // Generate the enchantment itself.
            if (enchantment != null) {
                dataGen.enchantments.add(target, enchantment);
                // Add to the correct tags for them to appear in the enchanting table.
                if (enchantment.definition.isTreasure) dataGen.enchantmentTags.add(EnchantmentTags.TREASURE, target);
                else dataGen.enchantmentTags.add(EnchantmentTags.NON_TREASURE, target);
            }

            // Generate general tags.
            if (!tags.isEmpty()) {
                for (TagKey<Enchantment> tag : tags) {
                    // Extra safety since treasure or non treasure is determined by the enchantment definition.
                    if (tag == EnchantmentTags.NON_TREASURE || tag == EnchantmentTags.TREASURE) {
                        continue;
                    }

                    dataGen.enchantmentTags.add(tag, target);
                }
            }
        }

        // endregion

        // region Translate

        private CompletableRegistryGetter<String> nameTranslation;
        private String descriptionTranslation;

        @Override
        public EnchantmentData translate(String translation) {
            nameTranslation = registries -> translation;
            return this;
        }

        @Override
        public EnchantmentData autoTranslate() {
            nameTranslation = registries -> StringHelper.toHumanReadableName(target.registryKey().getValue());
            return this;
        }

        public EnchantmentData translateDescription(String translation) {
            descriptionTranslation = translation;
            return this;
        }

        // endregion

        // region Enchantment

        private CompletableEnchantment enchantment;

        public CompletableEnchantment enchantment(TagKey<Item> supportedItems) {
            return new CompletableEnchantment(this, RegistrationUtils.itemTag(supportedItems));
        }

        // endregion

        // region Tags

        private final List<TagKey<Enchantment>> tags = new ArrayList<>();

        public EnchantmentData tag(TagKey<Enchantment> tag) {
            tags.add(tag);
            return this;
        }

        // endregion
    }

    // endregion

    // region Util

    @SuppressWarnings("unused")
    public interface RegistrationUtils {
        static <T> RegistryWrapper.Impl<T> getRegistry(RegistryWrapper.@NotNull WrapperLookup registries, RegistryKey<? extends Registry<T>> registryKey) {
            return registries.getOrThrow(registryKey);
        }

        static CompletableRegistryGetter<RegistryEntryList<Item>> itemTag(TagKey<Item> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ITEM).getOrThrow(tag);
        }

        static CompletableRegistryGetter<RegistryEntryList<DamageType>> damageTag(TagKey<DamageType> tag) {
            return registries -> getRegistry(registries, RegistryKeys.DAMAGE_TYPE).getOrThrow(tag);
        }

        static CompletableRegistryGetter<RegistryEntryList<Enchantment>> enchantmentTag(TagKey<Enchantment> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ENCHANTMENT).getOrThrow(tag);
        }

        static CompletableRegistryGetter<RegistryEntryList<Block>> blockTag(TagKey<Block> tag) {
            return registries -> getRegistry(registries, RegistryKeys.BLOCK).getOrThrow(tag);
        }

        static CompletableRegistryGetter<RegistryEntryList<EntityType<?>>> entityTypeTag(TagKey<EntityType<?>> tag) {
            return registries -> getRegistry(registries, RegistryKeys.ENTITY_TYPE).getOrThrow(tag);
        }
    }

    // endregion

    // region Structures

    @SuppressWarnings("unused")
    public static final class CompletableEnchantment {
        private final EnchantmentData parent;
        private final CompletableDefinition definition;
        private CompletableRegistryGetter<RegistryEntryList<Enchantment>> exclusiveSet = lookup -> RegistryEntryList.of();
        private final Map<ComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final ComponentMap.Builder effectMap = ComponentMap.builder();

        private CompletableEnchantment(EnchantmentData parent, CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems) {
            this.parent = parent;
            this.definition = new CompletableDefinition(supportedItems);
        }

        public EnchantmentData build() {
            parent.enchantment = this;
            return this.parent;
        }

        public CompletableEnchantment exclusiveSet(TagKey<Enchantment> exclusiveSet) {
            this.exclusiveSet = RegistrationUtils.enchantmentTag(exclusiveSet);
            return this;
        }

        public <E> @NotNull CompletableEnchantment addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect, LootCondition.Builder requirements) {
            getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NotNull CompletableEnchantment addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect) {
            this.getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.empty()));
            return this;
        }

        public <E> @NotNull CompletableEnchantment addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect, LootCondition.Builder requirements) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NotNull CompletableEnchantment addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.empty()));
            return this;
        }

        public @NotNull CompletableEnchantment addEffect(ComponentType<List<AttributeEnchantmentEffect>> type, AttributeEnchantmentEffect effect) {
            this.getEffectsList(type).add(effect);
            return this;
        }

        public <E> @NotNull CompletableEnchantment addNonListEffect(ComponentType<E> type, E effect) {
            this.effectMap.add(type, effect);
            return this;
        }

        public @NotNull CompletableEnchantment addEffect(ComponentType<Unit> type) {
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

        public CompletableEnchantment primaryItems(TagKey<Item> items) {
            definition.primaryItems = Optional.ofNullable(items == null ? null : RegistrationUtils.itemTag(items));
            return this;
        }

        public CompletableEnchantment weight(int weight) {
            definition.weight = weight;
            return this;
        }

        public CompletableEnchantment maxLevel(int maxLevel) {
            definition.maxLevel = maxLevel;
            return this;
        }

        public CompletableEnchantment minCost(int base, int perLevelAboveFirst) {
            definition.minCost = new Enchantment.Cost(base, perLevelAboveFirst);
            return this;
        }

        public CompletableEnchantment maxCost(int base, int perLevelAboveFirst) {
            definition.maxCost = new Enchantment.Cost(base, perLevelAboveFirst);
            return this;
        }

        public CompletableEnchantment anvilCost(int anvilCost) {
            definition.anvilCost = anvilCost;
            return this;
        }

        public CompletableEnchantment isTreasure(boolean isTreasure) {
            definition.isTreasure = isTreasure;
            return this;
        }

        public CompletableEnchantment addSlot(AttributeModifierSlot slot) {
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
        public static class CompletableDefinition {
            public final List<AttributeModifierSlot> slots = new ArrayList<>();
            public final CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems;
            public Optional<CompletableRegistryGetter<RegistryEntryList<Item>>> primaryItems = Optional.empty();
            public int weight = 1;
            public int maxLevel = 1;
            public Enchantment.Cost minCost = new Enchantment.Cost(1, 1);
            public Enchantment.Cost maxCost = new Enchantment.Cost(1, 1);
            public int anvilCost = 1;
            public boolean isTreasure;

            private CompletableDefinition(CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems) {
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

    // endregion
}
