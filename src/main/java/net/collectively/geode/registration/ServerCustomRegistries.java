package net.collectively.geode.registration;

import java.util.ArrayList;
import java.util.List;

interface ServerCustomRegistries {
    List<GeodeGroup> GROUPS = new ArrayList<>();

    static void postInitialization() {
        for (GeodeGroup group : GROUPS) {
            group.register();
        }
    }
}
