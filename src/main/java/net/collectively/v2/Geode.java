package net.collectively.v2;

@SuppressWarnings("unused")
public class Geode implements Registerer {
    private final String linkedModId;

    /// Creates a new [Geode] instance to be used for the given mod.
    public static Geode create(String linkedModId) {
        return new Geode(linkedModId);
    }

    protected Geode(String linkedModId) {
        this.linkedModId = linkedModId;
    }

    @Override
    public String getLinkedModId() {
        return linkedModId;
    }
}