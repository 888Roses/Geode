package net.collectively.geode.v2.util.selectors;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Function;

public interface EntityMappers {
    Function<Entity, Entity> UNARY = entity -> entity;
    Function<Entity, LivingEntity> LIVING = entity -> (LivingEntity) entity;
    Function<Entity, PlayerEntity> PLAYER = entity -> (PlayerEntity) entity;
    Function<Entity, ServerPlayerEntity> SERVER_PLAYER = entity -> (ServerPlayerEntity) entity;
    Function<Entity, ClientPlayerEntity> CLIENT_PLAYER = entity -> (ClientPlayerEntity) entity;
}
