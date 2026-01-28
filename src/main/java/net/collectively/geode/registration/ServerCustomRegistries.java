package net.collectively.geode.registration;

import java.util.ArrayList;
import java.util.List;

interface ServerCustomRegistries {
    List<GeodeItemGroup> GROUPS = new ArrayList<>();

    static void postInitialization() {
        for (GeodeItemGroup group : GROUPS) {
            group.register();
        }
    }
}
