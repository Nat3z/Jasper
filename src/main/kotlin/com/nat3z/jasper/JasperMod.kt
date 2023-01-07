package com.nat3z.jasper

import cc.blendingMC.commands.BlendingCommandHandler
import com.nat3z.jasper.commands.JasperCommand
import com.nat3z.jasper.config.JasperConfig
import com.nat3z.jasper.config.JasperConfigBlend
import com.nat3z.jasper.gui.UpdateFoundGui
import com.nat3z.jasper.impls.hooks.EventHandler
import com.nat3z.jasper.impls.hooks.MinecraftHook
import com.nat3z.jasper.utils.SkyUtils
import gg.blendingMC.BlendingMC
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(modid = JasperMod.MODID, name = "Jasper", version = JasperMod.VERSION, clientSideOnly = true)
class JasperMod {
    companion object {
        val config = JasperConfig
        @JvmField
        val LOGGER: Logger = LogManager.getLogger("Jasper Logger")!!
        val IS_UNSTABLE = true
        val hudPlacement: JasperConfigBlend = JasperConfigBlend()
        var mc = Minecraft.getMinecraft()
        var guiScreen: GuiScreen? = null
        const val MODID = "jasper"
        const val VERSION = "BETA-0.6"
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        if (SkyUtils.preparedupdate) {
            GuiUtil.open(UpdateFoundGui(MinecraftHook.updateVersion))
        }
        BlendingMC.getInstance().fmlinitialize()
        config.init()

        hudPlacement.updateConfigVariables()
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventHandler())
    }

    @Mod.EventHandler
    fun loadComplete(event: FMLLoadCompleteEvent) {
        BlendingCommandHandler.registerCommand(JasperCommand())
    }


    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (guiScreen != null) {
            mc.displayGuiScreen(guiScreen)
            guiScreen = null
        }
    }
}