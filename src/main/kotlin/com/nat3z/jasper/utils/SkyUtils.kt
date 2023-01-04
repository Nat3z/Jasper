package com.nat3z.jasper.utils

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import gg.essential.universal.ChatColor
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StringUtils
import java.io.File
import java.util.stream.Collectors

object SkyUtils {
    val mc = Minecraft.getMinecraft()
    var preparedupdate = false
    fun sendMessage(text: String) {
        mc.thePlayer.addChatMessage(ChatComponentText(ChatColor.GOLD + "[Jasper] " + ChatColor.YELLOW + text))
    }
    val generalFolder: File
        get() {
            val folder = File(Minecraft.getMinecraft().mcDataDir.toString() + "\\Jasper\\")
            if (!folder.exists()) folder.mkdir()
            return folder
        }

    val isInSkyblock: Boolean
        get() {
            if (Minecraft.getMinecraft().currentServerData != null &&
                Minecraft.getMinecraft().currentServerData.serverIP != null &&
                Minecraft.getMinecraft().currentServerData.serverIP.contains("hypixel.net")) {
                if (Minecraft.getMinecraft().thePlayer.worldScoreboard.getObjectiveInDisplaySlot(1) == null) return false
                val sbTitle = EnumChatFormatting.getTextWithoutFormattingCodes(Minecraft.getMinecraft().thePlayer.worldScoreboard.getObjectiveInDisplaySlot(1).displayName)
                if (sbTitle.startsWith("SKYBLOCK")) {
                    return true
                }
            }
            return false
        }

    val isInDungeons: Boolean
        get() {
            try {
                if (isInSkyblock) {
                    val scoreboard = scoreboardLines

                    for (s in scoreboard) {
                        val sCleaned = cleanSB(s)
                        if (sCleaned.contains("Dungeon Cleared"))
                            return true
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

    fun cleanSB(scoreboard: String): String {
        val nvString = StringUtils.stripControlCodes(scoreboard).toCharArray()
        val cleaned = StringBuilder()

        for (c in nvString) {
            if (c.toInt() > 20 && c.toInt() < 127)
                cleaned.append(c)
        }

        return cleaned.toString()
    }

    val scoreboardLines: List<String>
        get() {
            val lines = ArrayList<String>()
            val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard ?: return lines

            val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return lines

            var scores = scoreboard.getSortedScores(objective)

            val list = scores.stream()
                .filter { input ->
                    input != null && input.playerName != null && !input.playerName
                        .startsWith("#")
                }
                .collect(Collectors.toList())

            if (list.size > 15)
                scores = Lists.newArrayList(Iterables.skip(list, scores.size - 15))
            else
                scores = list

            for (score in scores) {
                val team = scoreboard.getPlayersTeam(score.playerName)
                lines.add(ScorePlayerTeam.formatPlayerName(team, score.playerName))
            }

            return lines
        }

    fun String.toChat(): ChatComponentText {
        return ChatComponentText(this)
    }
}