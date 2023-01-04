package com.nat3z.jasper.gui

import cc.blendingMC.vicious.BlendingHUDEditor
import com.nat3z.jasper.JasperMod
import gg.essential.api.EssentialAPI
import gg.essential.api.utils.GuiUtil
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RainbowColorConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*

class JasperGeneralGui : WindowScreen(ElementaVersion.V2, newGuiScale = EssentialAPI.getGuiUtil().getGuiScale()) {

    private val jasperText: UIText =
        UIText("Jasper").childOf(window).constrain {
            x = CenterConstraint()
            y = RelativeConstraint(0.075f)
            textScale = basicTextScaleConstraint { window.getHeight() }
        }

    init {
        GeneralButton("Config").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(.075f)
            width = 200.pixels()
            height = 20.pixels()
        }.onMouseClick {
            GuiUtil.open(JasperMod.config.gui())
        }

        GeneralButton("Edit Gui Locations").childOf(window).constrain {
            x = basicXConstraint { (window.getWidth() - this.getWidth()) - 3 }
            y = basicYConstraint { window.getHeight() - this.getHeight() - 20 }
            width = 200.pixels()
            height = 20.pixels()
        }.onMouseClick {
            JasperMod.guiScreen = BlendingHUDEditor(JasperMod.hudPlacement)
        }

        UIText("Made by Nat3zDev, Powered by Essential, Vicious, and BlendingMC").childOf(window).constrain {
            x = CenterConstraint()
            y = basicYConstraint { window.getHeight() - this.getHeight() - 5 }
        }

        animate()
    }

    private fun animate() {
        jasperText.animate {
            setTextScaleAnimation(Animations.OUT_EXP, 0.5f, basicTextScaleConstraint { window.getHeight() / 50 })
            setColorAnimation(Animations.IN_OUT_SIN, 1f, RainbowColorConstraint())
                .onComplete {
                    animate()
                }
        }
    }
}