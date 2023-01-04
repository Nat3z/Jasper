package com.nat3z.jasper.commands

import com.nat3z.jasper.gui.JasperGeneralGui
import gg.essential.api.utils.GuiUtil
import net.minecraft.client.entity.EntityPlayerSP

class JasperCommand : BaseCommand("jasper", listOf("jp", "jasp")) {
    override fun processCommand(player: EntityPlayerSP, args: Array<String>) {
        println("Opening config gui...")
        GuiUtil.open(JasperGeneralGui())
    }
}