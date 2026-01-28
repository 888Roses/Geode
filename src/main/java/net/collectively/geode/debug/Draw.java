package net.collectively.geode.debug;

import net.collectively.geode.types.double3;
import net.minecraft.client.render.DrawStyle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.debug.gizmo.GizmoDrawing;
import net.minecraft.world.debug.gizmo.TextGizmo;
import net.minecraft.world.debug.gizmo.VisibilityConfigurable;

/// Collection of debug Gizmo drawing utilities to draw boxes, lines, arrows, etc.
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Draw {
    /// Draws a box at the given [position][double3], with the given [size][double3] using the hexadecimal `Integer` color.
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param color The color of the lines of the drawn box. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param cornercoloredStroke Whether to color the corners of the box with the X, Y and Z colors or not.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(double3 position, double3 size, int color, boolean cornercoloredStroke) {
        return GizmoDrawing.box(
                Box.of(position.toVec3d(), size.x(), size.y(), size.z()),
                DrawStyle.stroked(color),
                cornercoloredStroke
        );
    }

    /// Draws a box at the given [position][double3], with the given [size][double3] using the hexadecimal `Integer` color.
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param color The color of the lines of the drawn box. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(double3 position, double3 size, int color) {
        return box(position, size, color, false);
    }

    /// Draws a white box at the given [position][double3], with the given [size][double3].
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param cornercoloredStroke Whether to color the corners of the box with the X, Y and Z colors or not.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(double3 position, double3 size, boolean cornercoloredStroke) {
        return box(position, size, 0xffFFFFFF, cornercoloredStroke);
    }

    /// Draws a white box at the given [position][double3], with the given [size][double3].
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(double3 position, double3 size) {
        return box(position, size, 0xffFFFFFF);
    }

    /// Draws a box at the given [block position][BlockPos], with the given [size][double3] using the hexadecimal `Integer` color.
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param color The color of the lines of the drawn box. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param cornercoloredStroke Whether to color the corners of the box with the X, Y and Z colors or not.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(BlockPos position, double3 size, int color, boolean cornercoloredStroke) {
        return box(new double3(position), size, color, cornercoloredStroke);
    }

    /// Draws a box at the given [block position][BlockPos], with the given [size][double3] using the hexadecimal `Integer` color.
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param color The color of the lines of the drawn box. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(BlockPos position, double3 size, int color) {
        return box(new double3(position), size, color);
    }

    /// Draws a white box at the given [block position][BlockPos], with the given [size][double3].
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @param cornercoloredStroke Whether to color the corners of the box with the X, Y and Z colors or not.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(BlockPos position, double3 size, boolean cornercoloredStroke) {
        return box(new double3(position), size, cornercoloredStroke);
    }

    /// Draws a white box at the given [block position][BlockPos], with the given [size][double3].
    /// @param position Where in the world this box should be drawn.
    /// @param size The size of the drawn box.
    /// @return The drawn gizmo.
    static VisibilityConfigurable box(BlockPos position, double3 size) {
        return box(new double3(position), size);
    }

    
    /// Draws a line from a [position a][double3] to a [position b][double3] with a given `width` and `color`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param color The color of the line. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param width How thick the line is.
    /// @return The drawn line.
    static VisibilityConfigurable line(double3 a, double3 b, int color, float width) {
        return GizmoDrawing.line(a.toVec3d(), b.toVec3d(), color, width);
    }

    /// Draws a line from a [position a][double3] to a [position b][double3] with a given `color`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param color The color of the line. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn line.
    static VisibilityConfigurable line(double3 a, double3 b, int color) {
        return line(a, b, color, 3);
    }

    /// Draws a white line from a [position a][double3] to a [position b][double3] with a given `width`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param width How thick the line is.
    /// @return The drawn line.
    static VisibilityConfigurable line(double3 a, double3 b, float width) {
        return GizmoDrawing.line(a.toVec3d(), b.toVec3d(), 0xffFFFFFF, width);
    }

    /// Draws a white line from a [position a][double3] to a [position b][double3].
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @return The drawn line.
    static VisibilityConfigurable line(double3 a, double3 b) {
        return GizmoDrawing.line(a.toVec3d(), b.toVec3d(), 0xffFFFFFF);
    }

    /// Draws a line from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `width` and `color`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param color The color of the line. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param width How thick the line is.
    /// @return The drawn line.
    static VisibilityConfigurable line(BlockPos a, BlockPos b, int color, float width) {
        return line(new double3(a), new double3(b), color, width);
    }

    /// Draws a line from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `color`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param color The color of the line. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn line.
    static VisibilityConfigurable line(BlockPos a, BlockPos b, int color) {
        return line(new double3(a), new double3(b), color);
    }

    /// Draws a white line from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `width`.
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @param width How thick the line is.
    /// @return The drawn line.
    static VisibilityConfigurable line(BlockPos a, BlockPos b, float width) {
        return line(new double3(a), new double3(b), width);
    }

    /// Draws a white line from a [block position a][BlockPos] to a [block position b][BlockPos].
    /// @param a The origin of the line.
    /// @param b The destination of the line.
    /// @return The drawn line.
    static VisibilityConfigurable line(BlockPos a, BlockPos b) {
        return line(new double3(a), new double3(b));
    }

    
    /// Draws an arrow from a [position a][double3] to a [position b][double3] with a given `width` and `color`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param color The color of the arrow. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param width How thick the arrow is.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(double3 a, double3 b, int color, float width) {
        return GizmoDrawing.arrow(a.toVec3d(), b.toVec3d(), color, width);
    }

    /// Draws an arrow from a [position a][double3] to a [position b][double3] with a given `color`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param color The color of the arrow. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(double3 a, double3 b, int color) {
        return line(a, b, color, 3);
    }

    /// Draws a white arrow from a [position a][double3] to a [position b][double3] with a given `width`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param width How thick the arrow is.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(double3 a, double3 b, float width) {
        return GizmoDrawing.arrow(a.toVec3d(), b.toVec3d(), 0xffFFFFFF, width);
    }

    /// Draws a white arrow from a [position a][double3] to a [position b][double3].
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(double3 a, double3 b) {
        return GizmoDrawing.arrow(a.toVec3d(), b.toVec3d(), 0xffFFFFFF);
    }

    /// Draws an arrow from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `width` and `color`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param color The color of the arrow. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @param width How thick the arrow is.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(BlockPos a, BlockPos b, int color, float width) {
        return arrow(new double3(a), new double3(b), color, width);
    }

    /// Draws an arrow from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `color`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param color The color of the arrow. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(BlockPos a, BlockPos b, int color) {
        return arrow(new double3(a), new double3(b), color);
    }

    /// Draws a white arrow from a [block position a][BlockPos] to a [block position b][BlockPos] with a given `width`.
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @param width How thick the arrow is.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(BlockPos a, BlockPos b, float width) {
        return arrow(new double3(a), new double3(b), width);
    }

    /// Draws a white arrow from a [block position a][BlockPos] to a [block position b][BlockPos].
    /// @param a The origin of the arrow.
    /// @param b The destination of the arrow.
    /// @return The drawn arrow.
    static VisibilityConfigurable arrow(BlockPos a, BlockPos b) {
        return arrow(new double3(a), new double3(b));
    }


    /// Draws a floating text facing the player at the given [position][double3] with the hexadecimal `Integer` color and containing the `String` text parsed from the `Object`.
    /// @param text Object representing the drawn text. It is then turned into a `String` using [String#valueOf(Object)].
    /// @param position Where in the world the text is drawn.
    /// @param color The color of the text. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn text gizmo.
    static VisibilityConfigurable text(Object text, double3 position, int color) {
        return GizmoDrawing.text(text.toString(), position.toVec3d(), TextGizmo.Style.left(color));
    }

    /// Draws a white floating text facing the player at the given [position][double3] containing the `String` text parsed from the `Object`.
    /// @param text Object representing the drawn text. It is then turned into a `String` using [String#valueOf(Object)].
    /// @param position Where in the world the text is drawn.
    /// @return The drawn text gizmo.
    static VisibilityConfigurable text(Object text, double3 position) {
        return text(text, position, 0xffFFFFFF);
    }

    /// Draws a floating text facing the player at the given [block position][BlockPos] with the hexadecimal `Integer` color and containing the `String` text parsed from the `Object`.
    /// @param text Object representing the drawn text. It is then turned into a `String` using [String#valueOf(Object)].
    /// @param position Where in the world the text is drawn.
    /// @param color The color of the text. Note that it should also specify the transparency of the gizmo using the first hexadecimal pair (i.e. `0xffFFFFFF`).
    /// @return The drawn text gizmo.
    static VisibilityConfigurable text(Object text, BlockPos position, int color) {
        return text(text, new double3(position), color);
    }

    /// Draws a white floating text facing the player at the given [block position][BlockPos] containing the `String` text parsed from the `Object`.
    /// @param text Object representing the drawn text. It is then turned into a `String` using [String#valueOf(Object)].
    /// @param position Where in the world the text is drawn.
    /// @return The drawn text gizmo.
    static VisibilityConfigurable text(Object text, BlockPos position) {
        return text(text, new double3(position));
    }
}