package net.collectively.geode.datagen.v2;

import net.collectively.geode.GeodeClient;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class GeodeDataGeneration extends BaseGeodeDataGeneration {
    private final Runnable collect;
    private final Supplier<GeodeClient> geode;

    public GeodeDataGeneration(FabricDataOutput dataOutput,
                               CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture,
                               Supplier<GeodeClient> geode,
                               @NotNull Runnable collect) {
        super(dataOutput, registriesFuture);
        this.geode = geode;
        this.collect = collect;
    }

    @Override
    protected void generate() {
        this.collect.run();
    }

    @Override
    public String getName() {
        if (geode == null || geode.get() == null) {
            return "Unknown Data Generator";
        }

        GeodeClient safeGeode = geode.get();
        assert safeGeode != null;

        return safeGeode.getLinkedModId() + " Data Generator";
    }
}
