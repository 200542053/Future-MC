package thedarkcolour.future.tile.barrel;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import thedarkcolour.future.init.Init;

public class TileBarrel extends TileEntity {
    public TileBarrel() {
        super(Init.TILE_BARREL);
    }

    private ItemStackHandler inventory = new ItemStackHandler(27);

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        if (compound.hasKey("items")) {
            this.getInventory().deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.setTag("items", this.getInventory().serializeNBT());
        return compound;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }
}