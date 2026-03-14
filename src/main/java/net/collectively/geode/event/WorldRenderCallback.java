package net.collectively.geode.event;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.ObjectAllocator;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/// Callback called when the [net.minecraft.client.render.WorldRenderer] renders the world.
/// @see net.minecraft.client.render.WorldRenderer#render(ObjectAllocator, RenderTickCounter, boolean, Camera, Matrix4f, Matrix4f, Matrix4f, GpuBufferSlice, Vector4f, boolean)
@Environment(EnvType.CLIENT)
public interface WorldRenderCallback {
    /// Event called before the world is rendered.
    Event<WorldRenderCallback> BEFORE = EventFactory.createArrayBacked(
            WorldRenderCallback.class,
            (listeners) -> (
                    ObjectAllocator allocator,
                    RenderTickCounter tickCounter,
                    boolean renderBlockOutline,
                    Camera camera,
                    Matrix4f positionMatrix,
                    Matrix4f basicProjectionMatrix,
                    Matrix4f projectionMatrix,
                    GpuBufferSlice fogBuffer,
                    Vector4f fogColor,
                    boolean renderSky
            ) -> {
                for (WorldRenderCallback listener : listeners) {
                    listener.render(allocator, tickCounter, renderBlockOutline, camera, positionMatrix, basicProjectionMatrix, projectionMatrix, fogBuffer, fogColor, renderSky);
                }
            }
    );

    /// Event called after the world is rendered.
    Event<WorldRenderCallback> AFTER = EventFactory.createArrayBacked(
            WorldRenderCallback.class,
            (listeners) -> (
                    ObjectAllocator allocator,
                    RenderTickCounter tickCounter,
                    boolean renderBlockOutline,
                    Camera camera,
                    Matrix4f positionMatrix,
                    Matrix4f basicProjectionMatrix,
                    Matrix4f projectionMatrix,
                    GpuBufferSlice fogBuffer,
                    Vector4f fogColor,
                    boolean renderSky
            ) -> {
                for (WorldRenderCallback listener : listeners) {
                    listener.render(allocator, tickCounter, renderBlockOutline, camera, positionMatrix, basicProjectionMatrix, projectionMatrix, fogBuffer, fogColor, renderSky);
                }
            }
    );

    void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f basicProjectionMatrix, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky);
}
