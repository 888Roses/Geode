package net.collectively.geode.cardinal_components.index;

import net.collectively.geode.Geode;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public abstract class EntityComponentIndex implements EntityComponentInitializer {
    public static <C extends Component> ComponentKey<C> keyOf(Identifier identifier, Class<C> componentClass) {
        return ComponentRegistry.getOrCreate(identifier, componentClass);
    }

    public static <C extends Component> ComponentKey<C> keyOf(String identifier, Class<C> componentClass) {
        return keyOf(Geode.id(identifier), componentClass);
    }

    @Override
    public final void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        register(new EntityComponentFactoryRegistryImpl(registry));
    }

    protected abstract void register(EntityComponentFactoryRegistryImpl registries);
}