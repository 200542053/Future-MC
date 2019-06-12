package thedarkcolour.future.tile.barrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBarrel extends Container {
    public final TileBarrel te;
    public final World world;
    public final BlockPos pos;

    public ContainerBarrel(InventoryPlayer playerInv, World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.te = (TileBarrel) world.getTileEntity(pos);

        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    private void addOwnSlots() {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new SlotItemHandler(te.getInventory(), col + row * 3, 8 + col * 18, 18 + row * 18));
            }
        }
    }

    private void addPlayerSlots(InventoryPlayer playerInv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18 - 1;
                int y = row * 18 + 70 + 15;
                addSlot(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotBar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18 - 1;
            int y = 58 + 70 + 15;
            this.addSlot(new Slot(playerInv, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        if (!(this.world.getTileEntity(pos) instanceof TileBarrel)) {
            return false;
        }
        else {
            return playerIn.getDistanceSq((double)this.te.getPos().getX() + 0.5D, (double)this.te.getPos().getY() + 0.5D, (double)this.te.getPos().getZ() + 0.5D) <= 64.0D;
        }
    }
}
