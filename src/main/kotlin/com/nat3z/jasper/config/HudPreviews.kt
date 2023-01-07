package com.nat3z.jasper.config

import cc.blendingMC.vicious.BlendingHudPreview
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import java.awt.Color

class HudPreviews {

    class GrandmaWolf : BlendingHudPreview {
        override fun run(x: Int, y: Int, w: Int, h: Int) {
            val mc = Minecraft.getMinecraft()
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Grandma Wolf: ${EnumChatFormatting.DARK_RED}${EnumChatFormatting.BOLD}EXPIRED", x.toFloat(), y.toFloat(), Color.white.rgb, true)
        }

    }
}