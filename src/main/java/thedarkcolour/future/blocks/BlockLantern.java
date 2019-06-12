package thedarkcolour.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLantern extends Block {
    public static final BooleanProperty HANGING = BooleanProperty.create("hanging");

    public BlockLantern() {
        super(Properties.create(Material.IRON));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(HANGING);
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();

        if(BlockFalling.canFallThrough(worldIn.getBlockState(pos))) {

        }
        return super.getStateForPlacement(context);
    }

    private boolean isBlockInvalid(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        return (block instanceof BlockBush) || BlockFalling.canFallThrough(worldIn.getBlockState(pos));
    }
}