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
            // Contains every already registered enchantment identifier, to check for duplicates.
            Set<Identifier> identifierSet = new HashSet<>();
            // Contains every serialized enchantments, ready to be written (when the future is complete).
            List<CompletableFuture<?>> serializedEnchantments = new ArrayList<>();

            Consumer<EnchantmentEntry> enchantmentSerializer = (enchantment) -> {
                if (!identifierSet.add(enchantment.identifier())) {
                    throw new IllegalStateException("Duplicate enchantment " + enchantment.identifier());
                }

                Path path = this.pathResolver.resolveJson(enchantment.identifier());
                serializedEnchantments.add(DataProvider.writeCodecToPath(
                        writer,
                        registries,
                        Enchantment.CODEC,
                        enchantment.value(),
                        path
                ));
            };

            // Writes every enchantment.
            for (EnchantmentGenerator enchantmentGenerator : this.enchantmentGenerators) {
                enchantmentGenerator.accept(registries, enchantmentSerializer);
            }

            return CompletableFuture.allOf(serializedEnchantments.toArray(CompletableFuture[]::new));
        });
    }

    public String getName() {
        return "Enchantments";
    }
}
