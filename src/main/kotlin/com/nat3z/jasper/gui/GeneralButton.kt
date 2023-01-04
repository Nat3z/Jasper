package com.nat3z.jasper.gui

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.universal.USound
import gg.essential.vigilance.utils.onLeftClick
import java.awt.Color

class GeneralButton(textString: String) : UIBlock(Color(0, 0, 0, 80)) {

    val text = UIText(textString).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(255, 255, 255).toConstraint()
    } childOf this

    init {
        this
            .constrain {
                width = (text.getWidth() + 10).pixels()
                height = (text.getHeight() + 10).pixels()
            }
            .onMouseEnter {
                animate {
                    setColorAnimation(
                        Animations.OUT_EXP,
                        0.5f,
                        Color(255, 255, 255, 80).toConstraint(),
                        0f
                    )
                }
                text.constrain {
                    color = Color(16777120).toConstraint()
                }
            }.onMouseLeave {
                animate {
                    setColorAnimation(
                        Animations.OUT_EXP,
                        0.5f,
                        Color(0, 0, 0, 80).toConstraint()
                    )
                }
                text.constrain {
                    color = Color(14737632).toConstraint()
                }
            }.onLeftClick {
                USound.playButtonPress()
            }

    }
}