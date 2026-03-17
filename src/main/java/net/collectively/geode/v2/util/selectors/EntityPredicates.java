package net.collectively.geode.v2.util.selectors;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface EntityPredicates {
    static boolean all(Entity entity) {return true;}
    static boolean none(Entity entity) {return false;}
    static boolean livingEntities(Entity entity) {return entity.isLiving();}
    static boolean players(Entity entity) {return entity.isPlayer();}
    static boolean serverPlayers(Entity entity) {return entity instanceof ServerPlayerEntity;}
    static boolean clientPlayers(Entity entity) {return entity instanceof ClientPlayerEntity;}
}
