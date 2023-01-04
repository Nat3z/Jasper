package com.nat3z.jasper.impls.reparty

import com.nat3z.jasper.JasperMod
import com.nat3z.jasper.utils.AsyncAwait
import com.nat3z.jasper.utils.Location
import com.nat3z.jasper.utils.SkyUtils
import gg.essential.api.EssentialAPI
import gg.essential.universal.USound
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class SpecificReparty {
    val mc = Minecraft.getMinecraft()
    var wasInDungeons = false

    var ticksCounted = 0
    fun tick(event: ClientTickEvent) {
        if (!JasperMod.config.autoreparty) return
        ticksCounted++
        if (ticksCounted != 200) return

        ticksCounted = 0

        if (!wasInDungeons && Location.currentLocation == Location.THE_CATACOMBS) {
            wasInDungeons = true
        }

    }

    fun swapWorld(event: WorldEvent.Load) {
        if (!wasInDungeons) return
        repartyUser()
        wasInDungeons = false
    }

    fun chat(event: ClientChatReceivedEvent) {
        if (!JasperMod.config.autoreparty) return
        val text = event.message.unformattedText

        if (!text.contains("> EXTRA STATS <") || text.contains(":")) return
        repartyUser()
    }

    private fun repartyUser() {
        Thread {
            val secondsUntilReparty = JasperMod.config.repartydelay * 1000
            USound.playPlingSound()
            SkyUtils.sendMessage("You have ${secondsUntilReparty / 1000} seconds until the user(s) will be repartied. This timer starts when you are in the dungeon hub")

            while (mc.theWorld != null && mc.thePlayer != null && Location.currentLocation != Location.DUNGEON_HUB)
            {
                Thread.sleep(100)
            }
            SkyUtils.sendMessage("You now have ${secondsUntilReparty / 1000} seconds until the user(s) will be repartied.")
            AsyncAwait.start({
                if (mc.theWorld == null || mc.thePlayer == null) return@start

                mc.thePlayer.sendChatMessage("/p invite " + JasperMod.config.autorepartyuser)
                USound.playPlingSound()
                EssentialAPI.getNotifications().push("Reparty", "${JasperMod.config.autorepartyuser} has been repartied.")
            }, secondsUntilReparty)
        }.start()


    }

    companion object {
        val INSTANCE = SpecificReparty()
    }
}