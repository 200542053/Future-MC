package thedarkcolour.future.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import thedarkcolour.future.FutureMC;

import static thedarkcolour.future.init.Init.*;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = FutureMC.MODID)
public class InitElements {
    @SubscribeEvent
    public static void registerBlock(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();

        r.register(SMOKER);
        r.register(BLAST_FURNACE);
        r.register(GRINDSTONE);
        r.register(BARREL);
    }

    @SubscribeEvent
    public static void registerItem(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        r.register(makeItemBlock(SMOKER));
        r.register(makeItemBlock(BLAST_FURNACE));
        r.register(makeItemBlock(GRINDSTONE));
        r.register(makeItemBlock(BARREL));
    }

    public static Item makeItemBlock(Block block) {
        return new ItemBlock(block, new Item.Properties().group(FUTURE_MC_TAB)).setRegistryName(block.getRegistryName());
    }

    @SubscribeEvent
    public static void registerTile(final RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TILE_BARREL);
        //TILE_BARREL = TileEntityType.register(FutureMC.MODID + ":barrel", TileEntityType.Builder.create(TileBarrel::new));
        //TILE_BLAST_FURNACE = TileEntityType.register(FutureMC.MODID + ":blast_furnace", TileEntityType.Builder.create(TileFurnaceAdvanced.BlastFurnace::new));
        //TILE_SMOKER = TileEntityType.register(FutureMC.MODID + ":smoker", TileEntityType.Builder.create(TileFurnaceAdvanced.Smoker::new));
    }
}