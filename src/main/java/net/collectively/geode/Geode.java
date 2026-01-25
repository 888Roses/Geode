package net.collectively.geode;

import net.collectively.geode.mc._internal.GeodeMinecraft;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main API class.
 */
public class Geode implements ModInitializer {
    public static final String GEODE_ID = "geode";

    public static String HOOKED_MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger(GEODE_ID);

    public static Identifier internalId(String identifier) {
        return Identifier.of(GEODE_ID, identifier);
    }

    public static Identifier id(String identifier) {
        return Identifier.of(HOOKED_MOD_ID, identifier);
    }

    public static void setHookedMod(String hookedModId) {
        HOOKED_MOD_ID = hookedModId;

        initializeInternals();
    }

    private static void initializeInternals() {

        OnInitializedCallback.EVENT.invoker().onInitialized();
    }

    @Override
    public void onInitialize() {
        GeodeMinecraft.initialize();
    }

    public interface OnInitializedCallback {
        Event<OnInitializedCallback> EVENT = EventFactory.createArrayBacked(
                OnInitializedCallback.class,
                listeners -> () -> {
                    for (OnInitializedCallback listener : listeners) {
                        listener.onInitialized();
                    }
                }
        );

        void onInitialized();
    }
}
