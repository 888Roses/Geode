package net.collectively.v2.registration;

import java.util.ArrayList;
import java.util.List;

interface GeodeCustomRegistries {
    List<GeodeGroup> GROUPS = new ArrayList<>();

    static void postInitialization() {
        for (GeodeGroup group : GROUPS) {
            group.register();
        }
    }
}
