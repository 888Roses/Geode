package net.collectively.geode.cardinal_components_api.foundation;

import net.minecraft.entity.Entity;

/**
 * {@link EnhancedComponent} holding a {@link Entity} target type.
 */
public abstract class EntityComponent implements EnhancedComponent<Entity> {
    protected final Entity entity;

    public EntityComponent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }
}