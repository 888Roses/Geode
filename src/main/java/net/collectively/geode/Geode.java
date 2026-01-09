package net.collectively.geode;

import net.collectively.geode.player.delays.PlayerDelays;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geode implements ModInitializer {
    public static final String GEODE_ID = "geode";
    public static final Logger LOGGER = LoggerFactory.getLogger(GEODE_ID);

    public static String MOD_ID;
    public static void hook(String modIdentifier) {
        LOGGER.info("Geode hooked on {}.", modIdentifier);
        MOD_ID = modIdentifier;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initialized Geode.");

        // TODO: Temporary!
        hook(GEODE_ID);
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            PlayerDelays.enqueue(playerEntity, 10, () -> playerEntity.sendMessage(Text.literal("Hi!"), false));
            return ActionResult.PASS;
        });
    }

    public static Identifier geodeId(String identifier) {
        return Identifier.of(GEODE_ID, identifier);
    }
    public static Identifier id(String identifier) {
        return Identifier.of(MOD_ID, identifier);
    }
}
