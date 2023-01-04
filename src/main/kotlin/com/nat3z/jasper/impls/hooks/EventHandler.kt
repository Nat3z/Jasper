package com.nat3z.jasper.impls.hooks

import com.nat3z.jasper.impls.reparty.SpecificReparty
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventHandler {

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        SpecificReparty.INSTANCE.chat(event)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        SpecificReparty.INSTANCE.tick(event)
    }

    @SubscribeEvent
    fun onSwapWorld(event: WorldEvent.Load) {
        SpecificReparty.INSTANCE.swapWorld(event)
    }
}