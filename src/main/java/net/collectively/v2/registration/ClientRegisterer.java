package net.collectively.v2.registration;

public interface ClientRegisterer extends CommonRegisterer {
    @Override
    default void register() {
    }
}
