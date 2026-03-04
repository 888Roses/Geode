package net.collectively.geode;

import net.collectively.geode.registration.ClientRegisterer;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class GeodeClient implements ClientRegisterer {
    private final String linkedModId;

    public Identifier id(String identifier) {
        return Identifier.of(linkedModId, identifier);
    }

    /// Creates a new [GeodeClient] instance to be used for the given mod.
    public static GeodeClient create(String linkedModId) {
        return new GeodeClient(linkedModId);
    }

    protected GeodeClient(String linkedModId) {
        this.linkedModId = linkedModId;
    }

    @Override
    public String getLinkedModId() {
        return linkedModId;
    }
}