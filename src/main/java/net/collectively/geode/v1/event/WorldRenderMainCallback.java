package net.collectively.geode.v1.event;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public interface WorldRenderMainCallback {
    Event<WorldRenderMainCallback> BEFORE = EventFactory.createArrayBacked(
            WorldRenderMainCallback.class,
            (listeners) -> (
                    FrameGraphBuilder frameGraphBuilder,
                    Frustum frustum,
                    Matrix4f posMatrix,
                    GpuBufferSlice fogBuffer,
                    boolean renderBlockOutline,
                    WorldRenderState state,
                    RenderTickCounter tickCounter,
                    Profiler profiler
            ) -> {
                for (WorldRenderMainCallback listener : listeners) {
                    listener.renderMain(
                            frameGraphBuilder,
                            frustum,
                            posMatrix,
                            fogBuffer,
                            renderBlockOutline,
                            state,
                            tickCounter,
                            profiler
                    );
                }
            }
    );

    Event<WorldRenderMainCallback> AFTER = EventFactory.createArrayBacked(
            WorldRenderMainCallback.class,
            (listeners) -> (
                    FrameGraphBuilder frameGraphBuilder,
                    Frustum frustum,
                    Matrix4f posMatrix,
                    GpuBufferSlice fogBuffer,
                    boolean renderBlockOutline,
                    WorldRenderState state,
                    RenderTickCounter tickCounter,
                    Profiler profiler
            ) -> {
                for (WorldRenderMainCallback listener : listeners) {
                    listener.renderMain(
                            frameGraphBuilder,
                            frustum,
                            posMatrix,
                            fogBuffer,
                            renderBlockOutline,
                            state,
                            tickCounter,
                            profiler
                    );
                }
            }
    );

    void renderMain(FrameGraphBuilder frameGraphBuilder,
                    Frustum frustum,
                    Matrix4f posMatrix,
                    GpuBufferSlice fogBuffer,
                    boolean renderBlockOutline,
                    WorldRenderState state,
                    RenderTickCounter tickCounter,
                    Profiler profiler);
}
