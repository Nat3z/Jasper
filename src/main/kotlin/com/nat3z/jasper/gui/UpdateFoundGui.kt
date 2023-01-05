package com.nat3z.jasper.gui

import com.nat3z.jasper.JasperMod
import com.nat3z.jasper.impls.hooks.MinecraftHook
import com.nat3z.jasper.utils.SkyUtils
import gg.essential.api.EssentialAPI
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.markdown.MarkdownComponent
import net.minecraft.client.gui.GuiMainMenu
import java.awt.Color

class UpdateFoundGui(version: String) : WindowScreen(ElementaVersion.V2, newGuiScale = 2) {
    init {
        UIBlock().childOf(window).constrain {
            color = Color(17, 24, 39).toConstraint()
            width = basicWidthConstraint { window.getWidth() }
            height = basicHeightConstraint { window.getHeight() }
        }
        UIText("Jasper $version is available!", shadow = false).childOf(window).constrain {
            x = CenterConstraint()
            y = CenterConstraint() - 100.pixels()
            textScale = 2f.pixels()
        }

        UIText("Current Version: ${JasperMod.VERSION}", shadow = false).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.025f)
            textScale = 1.5f.pixels()
        }

        var latestVersionText = UIText("Latest Version: $version", shadow = false).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.025f)
            textScale = 1.5f.pixels()
        }

        var releaseNoteWrapper = ScrollComponent().childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.025f)
            height = basicHeightConstraint { window.getHeight() - 90 - latestVersionText.getBottom() }
            width = RelativeConstraint(0.7f)
            color = Color(31, 41, 55).toConstraint()
        }

        MarkdownComponent(MinecraftHook.updateMarkdown).childOf(releaseNoteWrapper).constrain {
            height = RelativeConstraint()
            width = RelativeConstraint()
        }

        GeneralButton("Update Now").childOf(window).constrain {
            x = CenterConstraint()
            y = basicYConstraint { window.getHeight() - this.getHeight() - 75 }
            width = 200.pixels()
            height = 20.pixels()
        }.onMouseClick {
            mc.shutdown()
            MinecraftHook.startUpdate()
        }

        GeneralButton("Update Later").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.025f)
            width = 200.pixels()
            height = 20.pixels()
        }.onMouseClick {
            JasperMod.guiScreen = GuiMainMenu()
            EssentialAPI.getNotifications().push("Update Later", "This update will perform when Minecraft closes.")
        }

        GeneralButton("Ignore Update").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.025f)
            width = 200.pixels()
            height = 20.pixels()
        }.onMouseClick {
            SkyUtils.preparedupdate = false
            JasperMod.guiScreen = GuiMainMenu()
            EssentialAPI.getNotifications().push("Update Ignored", "You will be notified of this update next time you launch Minecraft.")
        }
    }
}