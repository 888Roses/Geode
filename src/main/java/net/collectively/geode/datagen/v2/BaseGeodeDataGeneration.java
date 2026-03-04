package net.collectively.geode.datagen.v2;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class BaseGeodeDataGeneration implements DataProvider {
    protected final GeodeDataGenerator generator;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;
    private final List<DataRunnable<?>> dataRunnable = new ArrayList<>();

    public BaseGeodeDataGeneration(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        generator = new GeodeDataGenerator(dataOutput, registriesFuture);
        this.registriesFuture = registriesFuture;
    }

    protected final <T extends DataRunnable<?>> T add(T runnable) {
        dataRunnable.add(runnable);
        //noinspection unchecked
        return (T) dataRunnable.getLast();
    }

    /// Collects every runnable before generating the datagen for them.
    protected abstract void generate();

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose((registries) -> {
            generate();

            // Enqueues the data generation into the given generator.
            dataRunnable.forEach(target -> target.collect(registries, generator));
            // Collects every data provider to generate datagen with.
            List<DataProvider> providers = new ArrayList<>();
            generator.collectDataProviders(registries, providers::add);
            // Generates the datagen.
            return CompletableFuture.allOf(providers.stream().map(x -> x.run(writer)).toArray(CompletableFuture[]::new));
        });
    }
}