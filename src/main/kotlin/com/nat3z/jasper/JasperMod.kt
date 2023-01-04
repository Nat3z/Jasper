package com.nat3z.jasper

import com.nat3z.jasper.commands.JasperCommand
import com.nat3z.jasper.impls.hooks.EventHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = JasperMod.MODID, name = "Syntils", version = JasperMod.VERSION, clientSideOnly = true)
class JasperMod {
    companion object {
        val config = JasperConfig
        @JvmField
        val LOGGER: Logger = LogManager.getLogger("Syntils Logger")!!
        val IS_UNSTABLE = false

        var mc = Minecraft.getMinecraft()
        var guiScreen: GuiScreen? = null
        const val MODID = "syntils"
        const val VERSION = "1.0.0"
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config.init()
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventHandler())
    }

    @Mod.EventHandler
    fun loadComplete(event: FMLLoadCompleteEvent) {
        var commandHandler = ClientCommandHandler.instance
        commandHandler.registerCommand(JasperCommand())
    }


    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (guiScreen != null) {
            mc.displayGuiScreen(guiScreen)
            guiScreen = null
        }
    }
}