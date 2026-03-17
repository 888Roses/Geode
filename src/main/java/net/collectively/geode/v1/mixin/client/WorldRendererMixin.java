package net.collectively.geode.v1.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.collectively.geode.v1.event.WorldRenderCallback;
import net.collectively.geode.v1.event.WorldRenderMainCallback;
import net.minecraft.client.render.*;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(at = @At("HEAD"), method = "render")
    private void geode$render$before(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f basicProjectionMatrix, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        WorldRenderCallback.BEFORE.invoker().render(
                allocator,
                tickCounter,
                renderBlockOutline,
                camera,
                positionMatrix,
                basicProjectionMatrix,
                projectionMatrix,
                fogBuffer,
                fogColor,
                renderSky
        );
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void geode$render$after(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f basicProjectionMatrix, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        WorldRenderCallback.AFTER.invoker().render(
                allocator,
                tickCounter,
                renderBlockOutline,
                camera,
                positionMatrix,
                basicProjectionMatrix,
                projectionMatrix,
                fogBuffer,
                fogColor,
                renderSky
        );
    }


    @Inject(at = @At("HEAD"), method = "renderMain")
    private void geode$renderMain$before(FrameGraphBuilder frameGraphBuilder, Frustum frustum, Matrix4f posMatrix, GpuBufferSlice fogBuffer, boolean renderBlockOutline, WorldRenderState state, RenderTickCounter tickCounter, Profiler profiler, CallbackInfo ci) {
        WorldRenderMainCallback.BEFORE.invoker().renderMain(
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

    @Inject(at = @At("TAIL"), method = "renderMain")
    private void geode$renderMain$after(FrameGraphBuilder frameGraphBuilder, Frustum frustum, Matrix4f posMatrix, GpuBufferSlice fogBuffer, boolean renderBlockOutline, WorldRenderState state, RenderTickCounter tickCounter, Profiler profiler, CallbackInfo ci) {
        WorldRenderMainCallback.AFTER.invoker().renderMain(
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
