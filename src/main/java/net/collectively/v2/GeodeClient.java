package net.collectively.v2;

import net.collectively.v2.registration.ClientRegisterer;

@SuppressWarnings("unused")
public class GeodeClient implements ClientRegisterer {
    private final String linkedModId;

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