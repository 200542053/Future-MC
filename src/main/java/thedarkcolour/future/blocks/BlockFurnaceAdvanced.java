package thedarkcolour.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Level;
import thedarkcolour.future.FutureMC;
import thedarkcolour.future.init.Init;
import thedarkcolour.future.tile.furnaceadvanced.TileFurnaceAdvanced;

import java.util.Random;

public class BlockFurnaceAdvanced extends Block {
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public BlockFurnaceAdvanced(FurnaceType type) {
        super(Properties.create(Material.ROCK));
        setRegistryName(FutureMC.MODID, type.getName());
        setDefaultState(getDefaultState().with(FACING, EnumFacing.NORTH).with(LIT, false));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(LIT)) {
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            if(worldIn.getBlockState(pos).getBlock() == Init.SMOKER) {
                double d0 = (double)pos.getX() + 0.5D;
                double d1 = (double)pos.getY();
                double d2 = (double)pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                }

                EnumFacing facing = stateIn.get(FACING);
                EnumFacing.Axis axis = facing.getAxis();
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                double d5 = axis == EnumFacing.Axis.X ? (double)facing.getXOffset() * 0.52D : d4;
                double d6 = rand.nextDouble() * 6.0D / 16.0D;
                double d7 = axis == EnumFacing.Axis.Z ? (double)facing.getZOffset() * 0.52D : d4;
                worldIn.spawnParticle(Particles.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle(Particles.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public static void setState(boolean active, World world, BlockPos pos) {
        BlockFurnaceAdvanced furnace = (BlockFurnaceAdvanced) world.getBlockState(pos).getBlock();

        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileFurnaceAdvanced) {
            EnumFacing facing = world.getBlockState(pos).get(BlockFurnaceAdvanced.FACING);

            world.setBlockState(pos, furnace.getDefaultState().with(FACING, facing).with(BlockFurnaceAdvanced.LIT, active));
        }
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.get(LIT) ? 15 : 0;
    }

    public enum FurnaceType implements IStringSerializable {
        SMOKER("smoker"),
        BLAST_FURNACE("blast_furnace");

        String regName;

        FurnaceType(String name) {
            this.regName = name;
        }

        public boolean canCraft(ItemStack stack) {
            if(this == BLAST_FURNACE) {
                return isOre(stack);
            }
            else if(this == SMOKER) {
                return isFood(stack);
            }
            else {
                FutureMC.LOGGER.log(Level.ERROR, "Error: cannot craft because type is not valid");
                return false;
            }
        }

        public boolean isFood(ItemStack stack) {
            return stack.getItem() instanceof ItemFood;
        }

        public boolean isOre(ItemStack stack) {
            return Tags.Items.ORES.contains(stack.getItem());
        }

        @Override
        public String getName() {
            return regName;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(FACING, LIT);
    }
}