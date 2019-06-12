package thedarkcolour.future.tile.barrel;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import thedarkcolour.future.FutureMC;

@OnlyIn(Dist.CLIENT)
public class GuiBarrel extends GuiContainer {
    public final TileBarrel te;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 167;

    private static final ResourceLocation background = new ResourceLocation(FutureMC.MODID, "textures/gui/barrel.png");

    public GuiBarrel(ContainerBarrel container) {
        super(container);
        this.te = container.te;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.Barrel"), 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.Inventory"), 8, this.ySize - 92, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}