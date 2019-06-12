package thedarkcolour.future.tile.furnaceadvanced;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.crafting.VanillaRecipeTypes;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import thedarkcolour.future.blocks.BlockFurnaceAdvanced;

public abstract class TileFurnaceAdvanced extends TileEntity implements ITickable {
    protected BlockFurnaceAdvanced.FurnaceType type; // The type of block this is
    protected boolean isBurning;
    public int fuelLeft, progress; // Time before current fuel burns out
    // The time this machine has been cooking

    protected ItemStackHandler inputCraft, inputFuel, outputCraft = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            TileFurnaceAdvanced.this.markDirty();
        }
    };

    protected CombinedInvWrapper wrapperInput = new CombinedInvWrapper(inputCraft, inputFuel);


    public TileFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType type) {
        super(TileEntityType.FURNACE);
        this.type = type;
    }

    protected void function() {
        if(!world.isRemote) {
            if(fuelLeft == 0) { // Skips if smelting
                if(trySmelt()) { // Checks if the machine has a valid recipe
                    startSmelt(); // Consumes fuel
                }
            }
            if(fuelLeft != 0 && this.type.canCraft(inputCraft.getStackInSlot(0))) { // Checks if the machine can operate
                doSmelt(); // Updates the progress, fuelTime, and isBurning
            }
            else {
                setIsBurning(false); // Stops burning if it cannot smelt
            }

            decreaseFuel();

            if(progress == 100) {
                finishSmelt(); // Consumes the input stack, inserts the output stack, and resets the progress
            }
            if(!this.type.canCraft(inputCraft.getStackInSlot(0)) || fuelLeft == 0) { // Cancels the craft cycle
                setIsBurning(false); // Fixes furnaces burning when not supposed to.
                progress = 0;
            }
        }
    }

    protected void setIsBurning(boolean isBurning) {
        if(this.isBurning != isBurning) {
            this.isBurning = isBurning;
            markDirty();
            BlockFurnaceAdvanced.setState(isBurning, world, pos);
            IBlockState state = world.getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public boolean trySmelt() {
        if(TileEntityFurnace.isItemFuel(fuelStack()) && this.type.canCraft(inputCraft.getStackInSlot(0))) {
            return putOutput(true);
        }
        return false;
    }

    public ItemStack fuelStack() {
        return inputFuel.getStackInSlot(0);
    }

    public void startSmelt() {
        setIsBurning(true);
        int burnTime = getItemBurnTime(fuelStack());
        fuelStack().shrink(1);
        fuelLeft = burnTime;

    }

    public void decreaseFuel() {
        if(fuelLeft != 0) {
            fuelLeft-=2;
        }
    }

    public void doSmelt() {
        progress++;
    }

    private void finishSmelt() {
        progress = 0;
        if(putOutput(false)) {
            inputCraft.extractItem(0, 1, false);
        }
    }

    private boolean putOutput(boolean simulate) {
        //noinspection ConstantConditions
        ItemStack result = this.world.getRecipeManager().getRecipe(new RecipeWrapper(wrapperInput,1,1), world, VanillaRecipeTypes.SMELTING).getRecipeOutput();
        ItemStack remaining = outputCraft.insertItem(0, result.copy(), simulate);

        return remaining.isEmpty();
    }

    @Override
    public void tick() {
        function();
    }

    public static int getItemBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime();
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? TileEntityFurnace.getBurnTimes().getOrDefault(item, 0) : ret);
        }
    }

    public static class BlastFurnace extends TileFurnaceAdvanced {
        public BlastFurnace() {
            super(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
        }
    }

    public static class Smoker extends TileFurnaceAdvanced {
        public Smoker() {
            super(BlockFurnaceAdvanced.FurnaceType.SMOKER);
        }
    }
}