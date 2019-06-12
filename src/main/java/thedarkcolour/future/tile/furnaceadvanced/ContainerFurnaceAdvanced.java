package thedarkcolour.future.tile.furnaceadvanced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thedarkcolour.future.blocks.BlockFurnaceAdvanced;

public class ContainerFurnaceAdvanced extends Container {
    protected InventoryPlayer playerInv;
    protected World world;
    protected BlockPos pos;

    public ContainerFurnaceAdvanced(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
        this.playerInv = playerInventory;
        this.world = worldIn;
        this.pos = posIn;

        //addOwnSlots();
        //addPlayerSlots();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (this.world.getBlockState(this.pos).getBlock() instanceof BlockFurnaceAdvanced) {
            return playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
        else {
            return false;
        }
    }
}