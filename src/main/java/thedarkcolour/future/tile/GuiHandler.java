package thedarkcolour.future.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import thedarkcolour.future.tile.barrel.ContainerBarrel;
import thedarkcolour.future.tile.barrel.GuiBarrel;
import thedarkcolour.future.tile.grindstone.ContainerGrindstone;
import thedarkcolour.future.tile.grindstone.GuiGrindstone;

public class GuiHandler {
    public static final int GUI_BARREL = 1;
    public static final int GUI_GRINDSTONE = 3;

    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();
        EntityPlayer player = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        System.out.println(openContainer.getAdditionalData().getInt(0));

        switch (openContainer.getAdditionalData().getInt(0)) {
            case GUI_GRINDSTONE:
                return new GuiGrindstone(new ContainerGrindstone(player.inventory, world, pos));
            case GUI_BARREL:
                return new GuiBarrel(new ContainerBarrel(player.inventory, world, pos));
        }
        return null;
    }
}