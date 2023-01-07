package com.nat3z.mixins;

import cc.blendingMC.vicious.BlendingConfig;
import cc.blendingMC.vicious.HudElement;
import cc.blendingMC.vicious.SerializeField;
import cc.blendingMC.vicious.SerializeType;
import com.nat3z.jasper.config.HudPreviews;

public class JasperConfigHUD implements BlendingConfig {
    @SerializeField(
            name = "grandma wolf hud toggle",
            description = "GWolf toggle",
            category = "HUD",
            subCategory = "Toggles",
            hidden = true,
            type = SerializeType.TOGGLE,
            UAYOR = false
    )
    public static boolean showGrandmaWolf = false;

    @SerializeField(
            name = "Grandma Wolf HUD",
            description = "GWolf HUD",
            category = "HUD",
            subCategory = "HUD",
            hidden = true,
            type = SerializeType.HUD,
            requiredElementToggled = "showGrandmaWolf",
            UAYOR = false
    )
    public static HudElement grandmaWolfHUD = new HudElement(30, 30, 150, 20, new HudPreviews.GrandmaWolf());
}
