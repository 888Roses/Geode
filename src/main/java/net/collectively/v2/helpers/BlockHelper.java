package net.collectively.v2.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

/// Collection of utilities regarding blocks.
@SuppressWarnings("unused")
public interface BlockHelper {
    /// Replaces the [blockState][BlockState] at the given [position][BlockPos] in the [world][World] with a modified version
    /// using the [unary operator modifier][UnaryOperator] of type [BlockState].
    ///
    /// @param world The world in which the blockState is.
    /// @param blockPos The position of the blockState in the world.
    /// @param modifier [UnaryOperator] modifying the blockState.
    /// @return The modified blockState.
    static BlockState modifyBlockState(World world, BlockPos blockPos, UnaryOperator<BlockState> modifier) {
        BlockState blockState = world.getBlockState(blockPos);
        BlockState modifiedBlockState = modifier.apply(blockState);

        world.setBlockState(blockPos, modifiedBlockState, Block.NOTIFY_ALL);
        world.onBlockStateChanged(blockPos, blockState, modifiedBlockState);

        return modifiedBlockState;
    }
}
