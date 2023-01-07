package com.nat3z.jasper.impls

import com.google.common.base.Stopwatch
import com.nat3z.jasper.JasperMod
import com.nat3z.jasper.utils.AsyncAwait
import com.nat3z.jasper.utils.SkyUtils
import com.nat3z.jasper.utils.WebUtils
import com.nat3z.mixins.JasperConfigHUD
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.awt.Color
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.ceil

class GrandmaWolf {
    val mc = Minecraft.getMinecraft()
    var stopwatch = Stopwatch.createUnstarted()
    var currentCombo = 5
    var combos = HashMap<Float, Float>().toMutableMap()
    var bestCombo = 0
    val comboPattern = Pattern.compile("(\\d+)\\b Combo \\(lasts (\\d+)s\\)")
    val comboMessagePattern = Pattern.compile("\\+\\s*(\\d+) Kill")
    val comboExpiredPattern = Pattern.compile("You reached a (\\d+) Kill Combo")
    var startTracking = false
    // this is in seconds
    var comboTimer = 8
    var totalTicks = 0

    fun chat(event: ClientChatReceivedEvent) {
        if (!JasperMod.config.grandmaWolfTimer) return
        val message = event.message.unformattedText
        if (!message.contains("Combo") || message.contains(":")) return
        val matcher = comboMessagePattern.matcher(message)

        while (matcher.find()) {
            val comboCount = matcher.group(1).toFloatOrNull()
            println("Fetched combo: $comboCount")
            if (comboCount == null) return
            currentCombo = comboCount.toInt()
            if (currentCombo >= 30)
                comboTimer = (combos[30f] ?: 0).toInt()
            else
                comboTimer = (combos[comboCount] ?: 0).toInt()
            return
        }

        val expiredMatcher = comboExpiredPattern.matcher(message)
        while (expiredMatcher.find()) {
            val comboCount = expiredMatcher.group(1).toFloatOrNull()
            println("Expired combo: $comboCount")
            if (comboCount == null) return
            if (bestCombo < comboCount)
                bestCombo = comboCount.toInt()
            currentCombo = 0
            comboTimer = 5
            startTracking = false
        }
    }
    var tickWhenGotCombatEXP = 0f
    var lastExpMeter = 0f
    fun expSoundEvent() {
        if (!JasperMod.config.grandmaWolfTimer) return

        AsyncAwait.start({
            // check if amount of ticks between totalTicks and tickWhenGotCombatEXP is less than 25
            // if it is, then stop this because it's probably a dupe on same tick

            if (totalTicks - tickWhenGotCombatEXP < 5) return@start
            /* check if player's exp meter has incremented. if so, most likely just gained exp. */
            val exp = mc.thePlayer.experience
            if (exp != lastExpMeter && exp != 0f) {
                return@start
            }

            lastExpMeter = exp
            tickWhenGotCombatEXP = totalTicks.toFloat()

            val actionBar = SkyUtils.actionBar
            if (!actionBar.contains(" Combat (")) return@start
            startTracking = true
            if (currentCombo == 0) {
                comboTimer = (combos[5f] ?: 0).toInt()
            }
            stopwatch.reset()
            stopwatch.start()
        }, 400)

    }


    fun tick() {
        if (!JasperMod.config.grandmaWolfTimer) return

        totalTicks++
        // prevent from reaching float limit for totalTicks
        if (totalTicks > 1000000) totalTicks = 0
        /* safety net to prevent combo */
        if (stopwatch.isRunning && stopwatch.elapsed(TimeUnit.SECONDS) > comboTimer + 2) {
            currentCombo = 0
            comboTimer = 5
            startTracking = false
            stopwatch.reset()
        }

        if (totalTicks % 40 == 0) {
            if (mc.thePlayer == null) return
            lastExpMeter = mc.thePlayer.experience
        }
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

                                val matcher = comboPattern.matcher(parsedLore)
                                try {
                                    while (matcher.find()) {
                                        val comboCount = matcher.group(1).toFloatOrNull()
                                        val comboDuration = matcher.group(2).toFloatOrNull()

                                        // subtract 3 because the Hypixel servers are somehow ahead of the timer? IDK how
                                        // the Grandma Wolf works tbh.
                                        if (comboCount != null && comboDuration != null)
                                            combos[comboCount] = comboDuration - 3
                                    }

                                    JasperMod.LOGGER.info("Successfully found loaded combos for Grandma Wolf.")
                                    return@fetch
                                } catch (ex: Exception) {
                                    JasperMod.LOGGER.info("Failed to parse combo.")
                                    JasperMod.LOGGER.error(ex)
                                }

                            }
                        }

                        if (mc.theWorld == null || mc.thePlayer == null) return@fetch
                        SkyUtils.sendMessage("Unable to find the Grandma Wolf pet for the current profile. Make sure that your API is on and that your Grandma Wolf is in your pets menu.")
                    }
                }
            }
        }.start()

    }

    fun render() {
        if (!JasperMod.config.grandmaWolfTimer) return

        var hud = JasperConfigHUD.grandmaWolfHUD
        var timeLeft = comboTimer
        if (stopwatch.isRunning)
            timeLeft = ceil((comboTimer - stopwatch.elapsed(TimeUnit.SECONDS)).toDouble()).toInt()

        // if combo timer has no more time left
        if (!startTracking) {
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Grandma Wolf: ${EnumChatFormatting.DARK_RED}${EnumChatFormatting.BOLD}EXPIRED", hud.x.toFloat(), hud.y.toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString("${EnumChatFormatting.BLUE}Best Combo: ${EnumChatFormatting.YELLOW}${bestCombo} Combo", hud.x.toFloat(), hud.y.toFloat() + 10, Color.white.rgb, true)
        } else {
            // use if operation to prevent negative numbers
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Grandma Wolf: ${EnumChatFormatting.DARK_RED}${if (timeLeft > 0) timeLeft else 0}s", hud.x.toFloat(), hud.y.toFloat(), Color.white.rgb, true)
            mc.fontRendererObj.drawString("${EnumChatFormatting.RED}Current Combo: ${EnumChatFormatting.DARK_RED}${currentCombo} Combo", hud.x.toFloat(), hud.y.toFloat() + 10, Color.white.rgb, true)
            mc.fontRendererObj.drawString("${EnumChatFormatting.BLUE}Best Combo: ${EnumChatFormatting.YELLOW}${bestCombo} Combo", hud.x.toFloat(), hud.y.toFloat() + 20, Color.white.rgb, true)
        }

        GlStateManager.resetColor()

    }

    companion object {
        val INSTANCE = GrandmaWolf()
    }
}