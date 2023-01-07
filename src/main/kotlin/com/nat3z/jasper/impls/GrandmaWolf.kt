package com.nat3z.jasper.impls

import com.google.common.base.Stopwatch
import com.nat3z.jasper.JasperMod
import com.nat3z.jasper.utils.SkyUtils
import com.nat3z.jasper.utils.WebUtils
import com.nat3z.mixins.JasperConfigHUD
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.ceil

class GrandmaWolf {
    val mc = Minecraft.getMinecraft()
    var stopwatch = Stopwatch.createUnstarted()
    var currentCombo = 0
    var combos = HashMap<Float, Float>().toMutableMap()

    val comboPattern = Pattern.compile("\\b(\\d+)\\b Combo \\(lasts (\\d+\\.\\d+)s\\)")
    val comboMessagePattern = Pattern.compile("\\+\\s*(\\d+)")

    // this is in seconds
    var comboTimer = 5
    fun chat(event: ClientChatReceivedEvent) {
        if (JasperMod.config.grandmaWolfTimer) return
        val message = event.message.unformattedTextForChat
        val matcher = comboMessagePattern.matcher(message)
        if (matcher.find()) {
            val comboCount = matcher.group(1).toFloatOrNull() ?: return
            currentCombo = comboCount.toInt()
            comboTimer = (combos[comboCount] ?: 0).toInt()
        }
    }

    var ticks = 0

    fun tick() {
        ticks++

        if (ticks < 25) return
        ticks = 0
        val actionBar = SkyUtils.actionBar
        if (!actionBar.contains(" Combat (")) return

        // reset the stopwatch and wait until time is reached...
        if (stopwatch.isRunning) {
            val timeLeft = ceil((comboTimer - stopwatch.elapsed(TimeUnit.SECONDS)).toDouble()).toInt()
            if (timeLeft > 0) return
            // restart stopwatch
            stopwatch.reset()
            stopwatch.start()
        }
        else
            stopwatch.start()
    }

    fun refreshGrandmaWolfTime() {
        Thread {
            WebUtils.fetch("https://sky.shiiyu.moe/api/v2/profile/${mc.session.profile.name}") { resUnparsed ->
                val res = resUnparsed.asJson()

                res["profiles"].asJsonObject.entrySet().forEach {
                    val json = it.value.asJsonObject
                    if (json["current"].asBoolean) {

                        json["data"].asJsonObject["pets"].asJsonArray.forEach { pet ->
                            if (pet.asJsonObject["type"].asString == "GRANDMA_WOLF") {
                                val lore = pet.asJsonObject["lore"].asString
                                var parsedLore = lore.replace("<span style='(.*?)'>|</span>|<span class=\\\"lore-row wrap\\\">".toRegex(), "")
                                println(parsedLore)
                                val matcher = comboPattern.matcher(parsedLore)
                                try {
                                    while (matcher.find()) {
                                        val comboCount = matcher.group(1).toFloatOrNull()
                                        val comboDuration = matcher.group(2).toFloatOrNull()

                                        JasperMod.LOGGER.info("Combo count: $comboCount")
                                        JasperMod.LOGGER.info("Combo duration: ${comboDuration}s")
                                        if (comboCount != null && comboDuration != null)
                                            combos[comboCount] = comboDuration
                                    }
                                    return@fetch
                                } catch (_: Exception) {
                                    JasperMod.LOGGER.info("Failed to parse combo.")
                                }

                            }
                        }
                    }
                }
            }
        }.start()

    }

    fun render() {
        var hud = JasperConfigHUD.grandmaWolfHUD
        var timeLeft = 0
        if (stopwatch.isRunning)
            timeLeft = ceil((comboTimer - stopwatch.elapsed(TimeUnit.SECONDS)).toDouble()).toInt()

        // if combo timer has no more time left
        if (timeLeft <= 0) {
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Grandma Wolf: ${EnumChatFormatting.DARK_RED}${EnumChatFormatting.BOLD}EXPIRED", hud.x.toFloat(), hud.y.toFloat(), Color.white.rgb, true)
        } else {
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Grandma Wolf: ${EnumChatFormatting.DARK_RED}${timeLeft}s", hud.x.toFloat(), hud.y.toFloat(), Color.white.rgb, true)
        }

    }

    companion object {
        val INSTANCE = GrandmaWolf()
    }
}