package net.collectively.geode.v1.datagen.v2;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class GeodeDataGenerator {
    public GeodeDataGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    }

    public void collectDataProviders(RegistryWrapper.WrapperLookup registries, Consumer<DataProvider> consumer) {
    }
}