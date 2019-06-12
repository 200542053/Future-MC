package thedarkcolour.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import thedarkcolour.future.tile.barrel.ContainerBarrel;
import thedarkcolour.future.tile.barrel.TileBarrel;

public class BlockBarrel extends Block {
    private static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockBarrel() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        setRegistryName("barrel");
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileBarrel)) return false;
        if(worldIn.isRemote) return true;

        NetworkHooks.openGui((EntityPlayerMP) player, new Interaction(worldIn, pos), (openBuffer) -> {
            openBuffer.writeInt(1);
            openBuffer.writeBlockPos(pos);
        });
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileBarrel();
    }

    public static class Interaction implements IInteractionObject {
        private final World worldIn;
        private final BlockPos pos;

        public Interaction(World worldIn, BlockPos pos) {
            this.worldIn = worldIn;
            this.pos = pos;
        }
        @Override
        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            return new ContainerBarrel(playerInventory, worldIn, pos);
        }

        @Override
        public String getGuiID() {
            return "futuremc:barrel";
        }

        @Override
        public ITextComponent getName() {
            return new TextComponentTranslation("container.Barrel");
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getCustomName() {
            return null;
        }
    }
}