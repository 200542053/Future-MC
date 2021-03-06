package com.herobrine.future.init.proxy;

import com.herobrine.future.entity.Entities;
import com.herobrine.future.init.InitModels;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @SubscribeEvent
    public static void registerModels(final ModelRegistryEvent event) {
        InitModels.initModel();
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        Entities.initModels();
    }
}