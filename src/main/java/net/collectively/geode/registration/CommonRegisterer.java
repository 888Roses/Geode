package net.collectively.geode.registration;

import net.minecraft.util.Identifier;

public interface CommonRegisterer {
    /// The name of the mod linked to this [ServerRegisterer].
    String getLinkedModId();

    /// Creates an [Identifier] using the [registerer's namespace][#getLinkedModId()] and the given `identifier` path.
    default Identifier id(String identifier) {
        return Identifier.of(getLinkedModId(), identifier);
    }

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
    void register();
}
