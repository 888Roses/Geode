package net.collectively.v2.datagen;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class GeodeEnchantmentGenerator implements DataProvider {
    public interface EnchantmentGenerator {
        void accept(RegistryWrapper.WrapperLookup registries, Consumer<EnchantmentEntry> exporter);
    }

    public record EnchantmentEntry(Identifier id, Enchantment value) {
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj instanceof EnchantmentEntry entry) {
                return entry.id().equals(id());
            }

            return false;
        }
    }

    private final DataOutput.PathResolver pathResolver;
    private final List<EnchantmentGenerator> enchantmentGenerators;
    private final CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture;

    public GeodeEnchantmentGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, List<EnchantmentGenerator> enchantmentGenerators) {
        this.pathResolver = output.getResolver(RegistryKeys.ENCHANTMENT);
        this.enchantmentGenerators = enchantmentGenerators;
        this.registriesFuture = registriesFuture;
    }

    public CompletableFuture<?> run(DataWriter writer) {
        return this.registriesFuture.thenCompose((registries) -> {
            Set<Identifier> set = new HashSet<>();
            List<CompletableFuture<?>> list = new ArrayList<>();
            Consumer<EnchantmentEntry> consumer = (enchantment) -> {
                if (!set.add(enchantment.id())) {
                    throw new IllegalStateException("Duplicate value " + enchantment.id());
                } else {
                    Path path = this.pathResolver.resolveJson(enchantment.id());
                    list.add(DataProvider.writeCodecToPath(writer, registries, Enchantment.CODEC, enchantment.value(), path));
                }
            };

            for (EnchantmentGenerator enchantmentGenerator : this.enchantmentGenerators) {
                enchantmentGenerator.accept(registries, consumer);
            }

            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }

    public String getName() {
        return "Enchantments";
    }
}
