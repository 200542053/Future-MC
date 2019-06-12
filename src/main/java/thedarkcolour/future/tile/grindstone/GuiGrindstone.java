package thedarkcolour.future.tile.grindstone;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import thedarkcolour.future.FutureMC;

@OnlyIn(Dist.CLIENT)
public class GuiGrindstone extends GuiContainer {
    private final ContainerGrindstone container;
    private static final int WIDTH = 176;
    private static final int HEIGHT = 166;

    private static final ResourceLocation background = new ResourceLocation(FutureMC.MODID,"textures/gui/grindstone.png");

    public GuiGrindstone(Container container) {
        super(container);
        this.container = (ContainerGrindstone) container;

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("container.Grindstone");
        this.fontRenderer.drawString(s, 8, 6, 4210752);
        this.fontRenderer.drawString(container.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 93, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(i, j, 0, 0, xSize, ySize);

        if(container.isRecipeInvalid) {
            this.drawTexturedModalRect(i + 92, j + 31, 176, 0, 28, 21);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}