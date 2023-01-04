package com.nat3z.jasper.commands

import cc.blendingMC.commands.*
import cc.blendingMC.vicious.BlendingHUDEditor
import com.nat3z.jasper.JasperMod
import com.nat3z.jasper.gui.JasperGeneralGui
import com.nat3z.jasper.utils.SkyUtils.toChat
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.Minecraft

@Command(name = "jasper", alias = ["jp", "jasp"])
class JasperCommand : BlendingCommand {
    @Argument
    var commandArgs: Array<String> = emptyArray()
    var mc = Minecraft.getMinecraft()
    @Runner
    fun runner() {
        commandArgs.forEach {
            mc.thePlayer.addChatComponentMessage(it.toChat())
        }
        if (commandArgs.isEmpty()) {
            println("Opening config gui...")
            GuiUtil.open(JasperGeneralGui())
        }
    }

    @Parameter(name = "hud")
    fun hud() {
        JasperMod.guiScreen = BlendingHUDEditor(JasperMod.hudPlacement)
    }
}