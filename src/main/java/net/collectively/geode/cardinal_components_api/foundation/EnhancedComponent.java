package net.collectively.geode.cardinal_components_api.foundation;

import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;

/**
 * Represents a component with extra features, notably:
 * <ul>
 *     <li>Easier client/server synchronisation using {@link #sync()} <i>(please note that this does not extend {@link org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent} by default. Please extend it for the syncing to work properly)</i>.</li>
 *     <li>Easy target retrieval using {@link #getEntity()}.</li>
 * </ul>
 *
 * @param <T> The type of object that holds this component.
 */
public interface EnhancedComponent<T> extends Component {
    /**
     * The object that holds this component.
     */
    T getEntity();

    /**
     * The registryKey representing the registered version of that component type.
     */
    ComponentKey<? extends Component> getKey();

    /**
     * Syncs this component between the client and server sides for the given target.
     *
     * @param target The object holding the component we wish to synchronise.
     * @apiNote Default implementation of this method is the same as doing {@link ComponentKey#sync(Object)}.
     */
    default void sync(T target) {
        this.getKey().sync(target);
    }

    /**
     * Syncs this {@code component} between the client and server sides for this component's {@link #getEntity()}.
     * @see #sync(Object)
     */
    default void sync() {
        this.sync(this.getEntity());
    }
}
