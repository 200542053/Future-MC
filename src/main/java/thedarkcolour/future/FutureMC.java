package thedarkcolour.future;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thedarkcolour.future.tile.GuiHandler;

@Mod(FutureMC.MODID)
public class FutureMC {
    public static final String MODID = "futuremc";
    public static final Logger LOGGER = LogManager.getLogger();

    public FutureMC() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
    }
}