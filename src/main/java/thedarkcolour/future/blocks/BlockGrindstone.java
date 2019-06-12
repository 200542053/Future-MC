package thedarkcolour.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.inventory.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import thedarkcolour.future.FutureMC;
import thedarkcolour.future.tile.grindstone.ContainerGrindstone;

public class BlockGrindstone extends Block implements ILiquidContainer {
    private static final EnumProperty<EnumAttachment> ATTACHMENT = EnumProperty.create("face", BlockGrindstone.EnumAttachment.class);
    private static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockGrindstone() {
        super(Properties.create(Material.ROCK));
        setRegistryName(FutureMC.MODID, "grindstone");
        setDefaultState(getDefaultState().with(ATTACHMENT, EnumAttachment.FLOOR).with(FACING, EnumFacing.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;

        if(!worldIn.isRemote) {
            NetworkHooks.openGui((EntityPlayerMP) player, new Interaction(worldIn, pos), packetBuffer -> {
                packetBuffer.writeInt(3);
                packetBuffer.writeBlockPos(pos);
            });
        }

        return true;
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(ATTACHMENT, FACING, WATERLOGGED);
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        EnumAttachment attachment = EnumAttachment.getFromFacing(context.getFace());
        EnumFacing finalFacing = context.getPlacementHorizontalFacing();
        if(attachment == EnumAttachment.WALL) {
            finalFacing = context.getFace();
        }

        return getDefaultState().with(ATTACHMENT, attachment).with(FACING, finalFacing);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) { // Wheel, SideA, SideB, LegA, LegB
        VoxelShape FLOOR_X = VoxelShapes.or(makeCuboidShape(2D,4D,4D,14D,16D,12D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(5D,7D,2D,11D,13D,4D), makeCuboidShape(5D,7D,14D,11D,13D,12D)), VoxelShapes.or(makeCuboidShape(6D,0D,12D,10D,7D,14D),makeCuboidShape(6D,0D,2D,10D,7D,4D))));
        VoxelShape FLOOR_Z = VoxelShapes.or(makeCuboidShape(4D,4D,2D,12D,16D,14D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(2D,7D,5D,4D,13D,11D), makeCuboidShape(14D,7D,5D,12D,13D,11D)), VoxelShapes.or(makeCuboidShape(12D,0D,6D,14D,7D,10D),makeCuboidShape(2D,0D,6D,4D,7D,10D))));
        VoxelShape CEILING_X = VoxelShapes.or(makeCuboidShape(2D,0D,4D,14D,12D,12D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(5D,3D,2D,11D,9D,4D), makeCuboidShape(5D,3D,14D,11D,9D,12D)), VoxelShapes.or(makeCuboidShape(6D,9D,12D,10D,16D,14D),makeCuboidShape(6D,9D,2D,10D,16D,4D))));
        VoxelShape CEILING_Z = VoxelShapes.or(makeCuboidShape(4D,0D,2D,12D,12D,14D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(2D,3D,5D,4D,9D,11D), makeCuboidShape(14D,3D,5D,12D,9D,11D)), VoxelShapes.or(makeCuboidShape(12D,9D,6D,14D,16D,10D),makeCuboidShape(2D,9D,6D,4D,16D,10D))));
        VoxelShape WALL_NORTH = VoxelShapes.or(makeCuboidShape(4D,2D,0D,12D,14D,12D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(2D,5D,3D,4D,11D,9D), makeCuboidShape(14D,5D,3D,12D,11D,9D)), VoxelShapes.or(makeCuboidShape(2D,6D,9D,4D,10D,16D),makeCuboidShape(14D,6D,9D,12D,10D,16D))));
        VoxelShape WALL_WEST = VoxelShapes.or(makeCuboidShape(0D,2D,4D,12D,14D,12D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(3D,5D,2D,9D,11D,4D), makeCuboidShape(3D,5D,14D,9D,11D,12D)), VoxelShapes.or(makeCuboidShape(9D,6D,2D,16D,10D,4D),makeCuboidShape(9D,6D,14D,16D,10D,12D))));
        VoxelShape WALL_SOUTH = VoxelShapes.or(makeCuboidShape(4D,2D,4D,12D,14D,16D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(2D,5D,7D,4D,11D,13D), makeCuboidShape(14D,5D,7D,12D,11D,13D)), VoxelShapes.or(makeCuboidShape(2D,6D,0D,4D,10D,7D),makeCuboidShape(14D,6D,0D,12D,10D,7D))));
        VoxelShape WALL_EAST = VoxelShapes.or(makeCuboidShape(4D,2D,4D,16D,14D,12D), VoxelShapes.or(VoxelShapes.or(makeCuboidShape(7D,5D,2D,13D,11D,4D), makeCuboidShape(7D,5D,14D,13D,11D,12D)), VoxelShapes.or(makeCuboidShape(0D,6D,2D,7D,10D,4D),makeCuboidShape(0D,6D,14D,7D,10D,12D))));

        switch (state.get(ATTACHMENT)) {
            case WALL: {
                switch (state.get(FACING)) {
                    case WEST: return WALL_WEST;
                    case SOUTH: return WALL_SOUTH;
                    case EAST: return WALL_EAST;
                    case NORTH: return WALL_NORTH;
                }
            }
            case FLOOR: return state.get(FACING) == EnumFacing.NORTH || state.get(FACING) == EnumFacing.SOUTH ? FLOOR_Z : FLOOR_X;
            case CEILING: return state.get(FACING) == EnumFacing.NORTH || state.get(FACING) == EnumFacing.SOUTH ? CEILING_Z : CEILING_X;
        }
        return VoxelShapes.fullCube();
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn) {
        if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            if (!worldIn.isRemote()) {
                worldIn.setBlockState(pos, state.with(WATERLOGGED, true), 3);
                worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    public enum EnumAttachment implements IStringSerializable {
        WALL("wall"), FLOOR("floor"), CEILING("ceiling");

        public String name;

        EnumAttachment(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static EnumAttachment getFromFacing(EnumFacing facing) {
            if(facing == EnumFacing.DOWN) {
                return CEILING;
            }
            if(facing == EnumFacing.UP) {
                return FLOOR;
            }
            return WALL;
        }
    }

    public static class Interaction implements IInteractionObject {
        private World world;
        private BlockPos pos;

        public Interaction(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        @Override
        public ITextComponent getName() {
            return new TextComponentTranslation("container.Grindstone");
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getCustomName() {
            return null;
        }

        @Override
        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            return new ContainerGrindstone(playerInventory, world, pos);
        }

        @Override
        public String getGuiID() {
            return "futuremc:grindstone";
        }
    }
}