package com.nat3z.jasper.impls.hooks

import com.nat3z.jasper.impls.GrandmaWolf
import com.nat3z.jasper.impls.reparty.SpecificReparty
import com.nat3z.jasper.utils.SkyUtils
import net.minecraft.client.Minecraft
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventHandler {

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        SpecificReparty.INSTANCE.chat(event)
        GrandmaWolf.INSTANCE.chat(event)
        if (event.type.toInt() === 2) {
            SkyUtils.actionBar = event.message.unformattedText
        }

    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        SpecificReparty.INSTANCE.tick(event)
        GrandmaWolf.INSTANCE.tick()
    }

    @SubscribeEvent
    fun render(event: RenderGameOverlayEvent.Post) {
        if (Minecraft.getMinecraft().ingameGUI !is GuiIngameForge) return
        if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE || event.type == RenderGameOverlayEvent.ElementType.JUMPBAR) {
            GrandmaWolf.INSTANCE.render()
        }
    }

    var firstJoin = true
    @SubscribeEvent
    fun onSwapWorld(event: WorldEvent.Load) {
        SpecificReparty.INSTANCE.swapWorld(event)
        SkyUtils.actionBar = ""
        GrandmaWolf.INSTANCE.refreshGrandmaWolfTime()
    }
}