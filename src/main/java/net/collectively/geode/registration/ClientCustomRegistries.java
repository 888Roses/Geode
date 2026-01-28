package net.collectively.geode.registration;

import java.util.ArrayList;
import java.util.List;

public interface ClientCustomRegistries {
    List<Option<?>> OPTIONS = new ArrayList<>();
    List<String> REMOVED_OPTIONS = new ArrayList<>();
}
