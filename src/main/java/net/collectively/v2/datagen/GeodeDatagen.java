package net.collectively.v2.datagen;

import net.collectively.v2.registration.GeodeEnchantment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
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
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public abstract class GeodeDatagen implements DataProvider {
    // region Essentials

    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public GeodeDatagen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        this.registriesFuture = registriesFuture;

        this.enchantmentTagProvider = new Internal_EnchantmentTagProvider(output, registriesFuture);
        this.enchantmentProvider = new Internal_EnchantmentProvider(output, registriesFuture);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate();
        return CompletableFuture.allOf(enchantmentTagProvider.run(writer), enchantmentProvider.run(writer));
    }

    @Override
    public String getName() {
        return "GeodeDatagen";
    }

    // endregion

    // region Resources

    private final Internal_EnchantmentTagProvider enchantmentTagProvider;
    private final Internal_EnchantmentProvider enchantmentProvider;

    /// Every [enchantment][EnchantmentEntry] registered in this data generator.
    private final List<IncompleteEnchantmentEntry> registeredEnchantmentEntries = new ArrayList<>();

    // endregion

    // region Registration

    protected abstract void generate();

    // region Enchantments

    protected final void addEnchantment(GeodeEnchantment geodeEnchantment, IncompleteEnchantmentBuilder enchantmentBuilder) {
        registeredEnchantmentEntries.add(new IncompleteEnchantmentEntry(
                geodeEnchantment.identifier(),
                enchantmentBuilder.build(geodeEnchantment.identifier())
        ));
    }
    protected final IncompleteEnchantmentBuilder enchantment(IncompleteDefinition incompleteDefinition) {
        return IncompleteEnchantment.builder(incompleteDefinition);
    }
    protected final IncompleteDefinitionBuilder enchantmentDefinition(CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems) {
        return IncompleteDefinition.builder(supportedItems);
    }

    protected record IncompleteEnchantment(Text description,
                                           IncompleteDefinition incompleteDefinition,
                                           CompletableRegistryGetter<RegistryEntryList<Enchantment>> exclusiveSet,
                                           ComponentMap effects) {
        public Enchantment complete(RegistryWrapper.WrapperLookup lookup) {
            return new Enchantment(description, incompleteDefinition.complete(lookup), exclusiveSet.complete(lookup), effects);
        }

        public static IncompleteEnchantmentBuilder builder(IncompleteDefinition incompleteDefinition) {
            return new IncompleteEnchantmentBuilder(incompleteDefinition);
        }
    }

    protected static class IncompleteEnchantmentBuilder {
        private final IncompleteDefinition incompleteDefinition;
        private CompletableRegistryGetter<RegistryEntryList<Enchantment>> exclusiveSet = lookup -> RegistryEntryList.of();
        private final Map<ComponentType<?>, List<?>> effectLists = new HashMap<>();
        private final ComponentMap.Builder effectMap = ComponentMap.builder();

        private IncompleteEnchantmentBuilder(IncompleteDefinition incompleteDefinition) {
            this.incompleteDefinition = incompleteDefinition;
        }

        public @NonNull IncompleteEnchantmentBuilder exclusiveSet(@NonNull CompletableRegistryGetter<RegistryEntryList<Enchantment>> incompleteExclusiveSet) {
            this.exclusiveSet = incompleteExclusiveSet;
            return this;
        }

        public <E> @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect, LootCondition.Builder requirements) {
            getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<List<EnchantmentEffectEntry<E>>> effectType, E effect) {
            this.getEffectsList(effectType).add(new EnchantmentEffectEntry<>(effect, Optional.empty()));
            return this;
        }

        public <E> @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect, LootCondition.Builder requirements) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.of(requirements.build())));
            return this;
        }

        public <E> @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<List<TargetedEnchantmentEffect<E>>> type, EnchantmentEffectTarget enchanted, EnchantmentEffectTarget affected, E effect) {
            this.getEffectsList(type).add(new TargetedEnchantmentEffect<>(enchanted, affected, effect, Optional.empty()));
            return this;
        }

        public @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<List<AttributeEnchantmentEffect>> type, AttributeEnchantmentEffect effect) {
            this.getEffectsList(type).add(effect);
            return this;
        }

        public <E> @NonNull IncompleteEnchantmentBuilder addNonListEffect(ComponentType<E> type, E effect) {
            this.effectMap.add(type, effect);
            return this;
        }

        public @NonNull IncompleteEnchantmentBuilder addEffect(ComponentType<Unit> type) {
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

        public IncompleteEnchantment build(Identifier id) {
            return new IncompleteEnchantment(
                    Text.translatable(Util.createTranslationKey("enchantment", id)),
                    this.incompleteDefinition,
                    this.exclusiveSet,
                    this.effectMap.build()
            );
        }
    }

    protected record IncompleteDefinition(CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems,
                                          Optional<CompletableRegistryGetter<RegistryEntryList<Item>>> primaryItems,
                                          int weight,
                                          int maxLevel,
                                          Enchantment.Cost minCost,
                                          Enchantment.Cost maxCost,
                                          int anvilCost,
                                          boolean isTreasure,
                                          List<AttributeModifierSlot> slots) {

        public Enchantment.Definition complete(RegistryWrapper.@NotNull WrapperLookup lookup) {
            return new Enchantment.Definition(
                    supportedItems.complete(lookup),
                    primaryItems.map(x -> x.complete(lookup)),
                    weight,
                    maxLevel,
                    minCost,
                    maxCost,
                    anvilCost,
                    slots
            );
        }

        public static IncompleteDefinitionBuilder builder(CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems) {
            return new IncompleteDefinitionBuilder(supportedItems);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected static class IncompleteDefinitionBuilder {
        private final List<AttributeModifierSlot> slots = new ArrayList<>();
        private final CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems;
        private Optional<CompletableRegistryGetter<RegistryEntryList<Item>>> primaryItems = Optional.empty();
        private int weight = 1;
        private int maxLevel = 1;
        private Enchantment.Cost minCost = new Enchantment.Cost(1, 1);
        private Enchantment.Cost maxCost = new Enchantment.Cost(1, 1);
        private int anvilCost = 1;
        private boolean isTreasure;

        private IncompleteDefinitionBuilder(CompletableRegistryGetter<RegistryEntryList<Item>> supportedItems) {
            this.supportedItems = supportedItems;
        }

        public @NonNull IncompleteDefinitionBuilder primaryItems(@Nullable CompletableRegistryGetter<RegistryEntryList<Item>> primaryItems) {this.primaryItems=Optional.ofNullable(primaryItems);return this;}
        public @NonNull IncompleteDefinitionBuilder weight(int weight) {this.weight=weight;return this;}
        public @NonNull IncompleteDefinitionBuilder maxLevel(int maxLevel) {this.maxLevel=maxLevel;return this;}
        public @NonNull IncompleteDefinitionBuilder minCost(int base, int perLevelAboveFirst) {this.minCost=new Enchantment.Cost(base,perLevelAboveFirst);return this;}
        public @NonNull IncompleteDefinitionBuilder maxCost(int base, int perLevelAboveFirst) {this.maxCost=new Enchantment.Cost(base,perLevelAboveFirst);return this;}
        public @NonNull IncompleteDefinitionBuilder anvilCost(int anvilCost) {this.anvilCost=anvilCost;return this;}
        public @NonNull IncompleteDefinitionBuilder addSlot(AttributeModifierSlot slot){this.slots.add(slot);return this;}
        public @NonNull IncompleteDefinitionBuilder isTreasure(boolean isTreasure) {this.isTreasure=isTreasure;return this;}

        public IncompleteDefinition build() {
            return new IncompleteDefinition(supportedItems, primaryItems, weight, maxLevel, minCost, maxCost, anvilCost, isTreasure, slots);
        }
    }

    // endregion

    // region Registration Util

    protected final <T> RegistryWrapper.Impl<T> getRegistry(RegistryWrapper.@NotNull WrapperLookup registries, RegistryKey<? extends Registry<T>> registryKey) {
        return registries.getOrThrow(registryKey);
    }
    protected final CompletableRegistryGetter<RegistryEntryList<Item>> itemTag(TagKey<Item> tag) {return registries -> getRegistry(registries, RegistryKeys.ITEM).getOrThrow(tag);}
    protected final CompletableRegistryGetter<RegistryEntryList<DamageType>> damageTag(TagKey<DamageType> tag) {return registries -> getRegistry(registries, RegistryKeys.DAMAGE_TYPE).getOrThrow(tag);}
    protected final CompletableRegistryGetter<RegistryEntryList<Enchantment>> enchantmentTag(TagKey<Enchantment> tag) {return registries -> getRegistry(registries, RegistryKeys.ENCHANTMENT).getOrThrow(tag);}
    protected final CompletableRegistryGetter<RegistryEntryList<Block>> blockTag(TagKey<Block> tag) {return registries -> getRegistry(registries, RegistryKeys.BLOCK).getOrThrow(tag);}
    protected final CompletableRegistryGetter<RegistryEntryList<EntityType<?>>> entityTypeTag(TagKey<EntityType<?>> tag) {return registries -> getRegistry(registries, RegistryKeys.ENTITY_TYPE).getOrThrow(tag);}

    // endregion

    // region Translation

    // endregion

    // endregion

    // region Providers

    @SuppressWarnings("unused")
    class Internal_EnchantmentTagProvider extends FabricTagProvider<Enchantment> {
        protected Internal_EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.@NonNull WrapperLookup wrapperLookup) {
            TagBuilder treasureBuilder = getTagBuilder(EnchantmentTags.TREASURE);
            TagBuilder nonTreasureBuilder = getTagBuilder(EnchantmentTags.NON_TREASURE);

            for (IncompleteEnchantmentEntry enchantmentEntry : registeredEnchantmentEntries) {
                TagBuilder builder = enchantmentEntry.incompleteEnchantment().incompleteDefinition().isTreasure()
                        ? treasureBuilder
                        : nonTreasureBuilder;

                builder.addOptional(enchantmentEntry.identifier());
            }

            treasureBuilder.build();
            nonTreasureBuilder.build();
        }
    }

    @SuppressWarnings("unused")
    class Internal_EnchantmentProvider extends GeodeEnchantmentGenerator {
        public Internal_EnchantmentProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture, List.of((registries, exporter) -> {
                for (IncompleteEnchantmentEntry enchantmentEntry : registeredEnchantmentEntries)
                    exporter.accept(enchantmentEntry.complete(registries));
            }));
        }
    }

    // endregion
}