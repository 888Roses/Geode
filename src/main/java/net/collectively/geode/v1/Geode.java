package net.collectively.geode.v1;

import net.collectively.geode.v1.registration.ServerRegisterer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Geode implements ServerRegisterer {
    private final String linkedModId;
    public final Logger logger;

    /// Creates a new [Geode] instance to be used for the given mod.
    public static Geode create(String linkedModId) {
        return new Geode(linkedModId);
    }

    protected Geode(String linkedModId) {
        this.linkedModId = linkedModId;
        this.logger = LoggerFactory.getLogger(linkedModId);
    }

    @Override
    public String getLinkedModId() {
        return linkedModId;
    }
}