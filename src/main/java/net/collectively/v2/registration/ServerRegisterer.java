package net.collectively.v2.registration;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface ServerRegisterer extends CommonRegisterer {
    // region Common

    /// Ends the registration stage of this [ServerRegisterer]. Should be called in the initialization step of your mod.
    /// For example:
    /// ```java
    /// public class ExampleMod implements ModInitializer {
    ///     public static final String MOD_ID = "example_mod";
    ///     public static final Geode geode = Geode.create(MOD_ID);
    ///
    ///     @Override
    ///     public void onInitialize() {
    ///         geode.register();
    ///     }
    /// }
    /// ```
    @Override
    default void register() {
        ServerCustomRegistries.postInitialization();
    }

    // endregion

    // region Items

    /// Registers a new [Item] using the given [identifier](Identifier), [factory](ItemFactory) and [settings](Item.Settings).
    ///
    /// @param identifier The identifier of the created item.
    /// @param factory Function creating an [Item] from a given [Item.Settings].
    /// @param settings The settings of the created item.
    /// @return The created item.
    ///
    /// @see #registerItem(String, ItemFactory, Item.Settings)
    default Item registerItem(Identifier identifier, ItemFactory factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, identifier);
        return Registry.register(Registries.ITEM, key, factory.createItem(settings.registryKey(key)));
    }

    /// Registers a new [Item] using the given `String` identifier, [factory](ItemFactory) and [settings](Item.Settings).
    ///
    /// @param identifier The identifier of the created item.
    /// @param factory Function creating an [Item] from a given [Item.Settings].
    /// @param settings The settings of the created item.
    /// @return The created item.
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerItem(Identifier, ItemFactory, Item.Settings)
    default Item registerItem(String identifier, ItemFactory factory, Item.Settings settings) {
        return registerItem(id(identifier), factory, settings);
    }

    /// Registers a new [Item] using the given [identifier](Identifier) and [settings](Item.Settings).
    /// The created item is of class [Item].
    ///
    /// @param identifier The identifier of the created item.
    /// @param settings The settings of the created item.
    /// @return The created item.
    ///
    /// @see #registerItem(String, Item.Settings)
    default Item registerItem(Identifier identifier, Item.Settings settings) {
        return registerItem(identifier, Item::new, settings);
    }

    /// Registers a new [Item] using the given [identifier](Identifier) and [settings](Item.Settings).
    /// The created item is of class [Item].
    ///
    /// @param identifier The identifier of the created item.
    /// @param settings The settings of the created item.
    /// @return The created item.
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerItem(Identifier, Item.Settings)
    default Item registerItem(String identifier, Item.Settings settings) {
        return registerItem(identifier, Item::new, settings);
    }

    /// Registers a new [Item] using the given [identifier](Identifier).
    /// The created item is of class [Item] using the default [Item.Settings].
    ///
    /// @param identifier The identifier of the created item.
    /// @return The created item.
    ///
    /// @see #registerItem(String)
    default Item registerItem(Identifier identifier) {
        return registerItem(identifier, new Item.Settings());
    }

    /// Registers a new [Item] using the given [identifier](Identifier).
    /// The created item is of class [Item] using the default [Item.Settings].
    ///
    /// @param identifier The identifier of the created item.
    /// @return The created item.
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerItem(Identifier)
    default Item registerItem(String identifier) {
        return registerItem(identifier, new Item.Settings());
    }

    /// Factory creating [Item] objects using given [Item.Settings].
    @FunctionalInterface
    interface ItemFactory {
        /// Creates an item from its settings.
        ///
        /// @param settings The settings of the item to create.
        /// @return The created item to be registered.
        Item createItem(Item.Settings settings);
    }

    // endregion

    // region Blocks

    /// Registers a new [Block] using the given [identifier](Identifier), [factory](BlockFactory) and [settings](AbstractBlock.Settings).
    ///
    /// @param identifier The identifier of the created block.
    /// @param factory Function creating an [Block] from a given [AbstractBlock.Settings].
    /// @param settings The settings of the created block.
    /// @return The created block.
    ///
    /// @see #registerBlock(String, BlockFactory, AbstractBlock.Settings)
    default Block registerBlock(Identifier identifier, BlockFactory factory, AbstractBlock.Settings settings) {
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        return Registry.register(Registries.BLOCK, key, factory.createBlock(settings.registryKey(key)));
    }

    /// Registers a new [Block] using the given [identifier](Identifier), [factory](BlockFactory) and [settings](AbstractBlock.Settings).
    ///
    /// @param identifier The identifier of the created block.
    /// @param factory Function creating an [Block] from a given [AbstractBlock.Settings].
    /// @param settings The settings of the created block.
    /// @return The created block.
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerBlock(Identifier, BlockFactory, AbstractBlock.Settings)
    default Block registerBlock(String identifier, BlockFactory factory, AbstractBlock.Settings settings) {
        return registerBlock(id(identifier), factory, settings);
    }

    /// Registers a new [Block] using the given [identifier](Identifier), [factory](BlockFactory) and [settings](AbstractBlock.Settings).
    /// The created block is of class [Block].
    ///
    /// @param identifier The identifier of the created block.
    /// @param settings The settings of the created block.
    /// @return The created block.
    ///
    /// @see #registerBlock(String, AbstractBlock.Settings)
    default Block registerBlock(Identifier identifier, AbstractBlock.Settings settings) {
        return registerBlock(identifier, Block::new, settings);
    }

    /// Registers a new [Block] using the given [identifier](Identifier), [factory](BlockFactory) and [settings](AbstractBlock.Settings).
    /// The created block is of class [Block].
    ///
    /// @param identifier The identifier of the created block.
    /// @param settings The settings of the created block.
    /// @return The created block.
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerBlock(Identifier, AbstractBlock.Settings)
    default Block registerBlock(String identifier, AbstractBlock.Settings settings) {
        return registerBlock(id(identifier), settings);
    }

    /// Factory creating [Block] objects using given [AbstractBlock.Settings].
    @FunctionalInterface
    interface BlockFactory {
        /// Creates a new [Block] instance from given [AbstractBlock.Settings] settings.
        ///
        /// @param settings The settings of the created block.
        /// @return The created block object.
        Block createBlock(AbstractBlock.Settings settings);
    }

    // endregion

    // region BlockEntities

    /// Registers a new [BlockEntity] using the given [identifier](Identifier), [factory](FabricBlockEntityTypeBuilder.Factory) and [blocks][Block].
    ///
    /// @param identifier The identifier of the created block entity.
    /// @param factory Factory function taking in the [position](BlockPos) and [BlockState] of the block entity and returning a new [BlockEntity] of type [T].
    /// @param blocks Any number of [Block] this block state applies to.
    /// @return A [BlockEntityType] object containing information about the registered [BlockEntity].
    ///
    /// @see #registerBlockEntity(String, FabricBlockEntityTypeBuilder.Factory, Block...)
    default <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Identifier identifier, FabricBlockEntityTypeBuilder.Factory<? extends @NotNull T> factory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, identifier, FabricBlockEntityTypeBuilder.<T>create(factory, blocks).build());
    }

    /// Registers a new [BlockEntity] using the given [identifier](Identifier), [factory](FabricBlockEntityTypeBuilder.Factory) and [blocks][Block].
    ///
    /// @param identifier The identifier of the created block entity.
    /// @param factory Factory function taking in the [position](BlockPos) and [BlockState] of the block entity and returning a new [BlockEntity] of type [T].
    /// @param blocks Any number of [Block] this block state applies to.
    /// @return A [BlockEntityType] object containing information about the registered [BlockEntity].
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerBlockEntity(Identifier, FabricBlockEntityTypeBuilder.Factory, Block...)
    default <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String identifier, FabricBlockEntityTypeBuilder.Factory<? extends @NotNull T> factory, Block... blocks) {
        return registerBlockEntity(id(identifier), factory, blocks);
    }

    // endregion

    // region Tags

    /// Creates a new [TagKey] of type [T] with the given [identifier](Identifier).
    ///
    /// @param registryReference The registry of this type.
    /// @param identifier The identifier of the tag.
    /// @return The created [TagKey].
    ///
    /// @see #registerTag(RegistryKey, String)
    default <T> TagKey<T> registerTag(RegistryKey<? extends Registry<T>> registryReference, Identifier identifier) {
        return TagKey.of(registryReference, identifier);
    }

    /// Creates a new [TagKey] of type [T] with the given [identifier](Identifier).
    ///
    /// @param registryReference The registry of this type.
    /// @param identifier The identifier of the tag.
    /// @return The created [TagKey].
    ///
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    ///
    /// @see #registerTag(RegistryKey, Identifier)
    default <T> TagKey<T> registerTag(RegistryKey<? extends Registry<T>> registryReference, String identifier) {
        return registerTag(registryReference, id(identifier));
    }

    /// Creates a new [TagKey] of type [Block] with the given [identifier](Identifier).
    ///
    /// @param identifier The identifier of that tag.
    /// @return The created [TagKey] of [Block].
    /// @see #registerBlockTag(String)
    default TagKey<Block> registerBlockTag(Identifier identifier) {
        return registerTag(RegistryKeys.BLOCK, identifier);
    }

    /// Creates a new [TagKey] of type [Block] with the given [identifier](Identifier).
    ///
    /// @param identifier The identifier of that tag.
    /// @return The created [TagKey] of [Block].
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    /// @see #registerBlockTag(Identifier)
    default TagKey<Block> registerBlockTag(String identifier) {
        return registerTag(RegistryKeys.BLOCK, identifier);
    }

    /// Creates a new [TagKey] of type [Item] with the given [identifier](Identifier).
    ///
    /// @param identifier The identifier of that tag.
    /// @return The created [TagKey] of [Item].
    /// @see #registerItemTag(String)
    default TagKey<Item> registerItemTag(Identifier identifier) {
        return registerTag(RegistryKeys.ITEM, identifier);
    }

    /// Creates a new [TagKey] of type [Item] with the given [identifier](Identifier).
    ///
    /// @param identifier The identifier of that tag.
    /// @return The created [TagKey] of [Item].
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    /// @see #registerItemTag(Identifier)
    default TagKey<Item> registerItemTag(String identifier) {
        return registerTag(RegistryKeys.ITEM, identifier);
    }

    // endregion

    // region Sounds

    /// Registers a new [SoundEvent] with the given [identifier](Identifier).
    ///
    /// @param identifier The identifier of the registered sound.
    /// @return The registered [SoundEvent].
    /// @see #registerSound(String)
    default SoundEvent registerSound(Identifier identifier) {
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    /// Registers a new [SoundEvent] with the given `String` identifier.
    ///
    /// @param identifier The identifier of the registered sound.
    /// @return The registered [SoundEvent].
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    /// @see #registerSound(Identifier)
    default SoundEvent registerSound(String identifier) {
        return registerSound(id(identifier));
    }

    // endregion

    // region EntityType

    /// Registers a new [entity type](EntityType) of type [T] with the given [Identifier] using the [EntityType.Builder].
    ///
    /// @param identifier The identifier of that entity.
    /// @param builder    Builder class containing information about the registration of that entity.
    /// @param <T>        The type of entity to register.
    /// @return The registered [EntityType] of type [T].
    /// @see #registerEntity(String, EntityType.Builder)
    default <T extends Entity> EntityType<T> registerEntity(Identifier identifier, EntityType.Builder<T> builder) {
        RegistryKey<EntityType<?>> registryKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, identifier);
        return Registry.register(Registries.ENTITY_TYPE, registryKey, builder.build(registryKey));
    }

    /// Registers a new [entity type](EntityType) of type [T] with the given [Identifier] using the [EntityType.Builder].
    ///
    /// @param identifier The identifier of that entity.
    /// @param builder    Builder class containing information about the registration of that entity.
    /// @param <T>        The type of entity to register.
    /// @return The registered [EntityType] of type [T].
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    /// @see #registerEntity(Identifier, EntityType.Builder)
    default <T extends Entity> EntityType<T> registerEntity(String identifier, EntityType.Builder<T> builder) {
        return registerEntity(id(identifier), builder);
    }

    // endregion

    // region ItemGroups

    /// Registers a new [ItemGroup] using the given [identifier][Identifier], [builder][ItemGroup.Builder] and [content][Item].
    ///
    /// @param identifier The identifier of the created group.
    /// @param builder A [builder][ItemGroup.Builder] describing the created item group.
    /// @param content A list of every [item][Item] contained in the group.
    /// @return The created [group][GeodeGroup].
    /// @see #registerGroup(String, ItemGroup.Builder, Item...)
    default GeodeGroup registerGroup(Identifier identifier, ItemGroup.Builder builder, Item... content) {
        RegistryKey<ItemGroup> registryKey = RegistryKey.of(Registries.ITEM_GROUP.getKey(), identifier);
        GeodeGroup constructedGroup = new GeodeGroup(registryKey, builder.build(), () -> content);
        ServerCustomRegistries.GROUPS.add(constructedGroup);
        return constructedGroup;
    }

    /// Registers a new [ItemGroup] using the given [identifier][Identifier], [builder][ItemGroup.Builder] and [content][Item].
    ///
    /// @param identifier The identifier of the created group.
    /// @param builder A [builder][ItemGroup.Builder] describing the created item group.
    /// @param content A list of every [item][Item] contained in the group.
    /// @return The created [group][GeodeGroup].
    /// @apiNote The `String` identifier is turned into an [Identifier] with the namespace being the [#getLinkedModId()],
    ///          using [#id(String)].
    /// @see #registerGroup(Identifier, ItemGroup.Builder, Item...)
    default GeodeGroup registerGroup(String identifier, ItemGroup.Builder builder, Item... content) {
        return registerGroup(id(identifier), builder, content);
    }

    // endregion
}
