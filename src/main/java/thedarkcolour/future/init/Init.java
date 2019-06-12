package thedarkcolour.future.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import thedarkcolour.future.blocks.BlockBarrel;
import thedarkcolour.future.blocks.BlockFurnaceAdvanced;
import thedarkcolour.future.blocks.BlockGrindstone;
import thedarkcolour.future.blocks.BlockLantern;
import thedarkcolour.future.tile.barrel.TileBarrel;

public final class Init {
    public static ItemGroup FUTURE_MC_TAB = new ItemGroup("Future") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(GRINDSTONE);
        }
    };

    public static final BlockFurnaceAdvanced SMOKER;
    public static final BlockFurnaceAdvanced BLAST_FURNACE;
    public static final BlockGrindstone GRINDSTONE;
    public static final BlockBarrel BARREL;
    public static final BlockLantern LANTERN;

    public static TileEntityType<?> TILE_BARREL = TileEntityType.Builder.create(TileBarrel::new).build(null).setRegistryName("futuremc:barrel");
    public static TileEntityType<?> TILE_BLAST_FURNACE;
    public static TileEntityType<?> TILE_SMOKER;

    static {
        SMOKER = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.SMOKER);
        BLAST_FURNACE = new BlockFurnaceAdvanced(BlockFurnaceAdvanced.FurnaceType.BLAST_FURNACE);
        GRINDSTONE = new BlockGrindstone();
        BARREL = new BlockBarrel();
        LANTERN = new BlockLantern();
    }
}