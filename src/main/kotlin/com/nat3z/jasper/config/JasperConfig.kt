package com.nat3z.jasper.config

import com.nat3z.mixins.JasperConfigHUD
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import java.io.File

object JasperConfig : Vigilant(
    File("./config/jasper.toml"),
    "Jasper",
    sortingBehavior = SortingBehavior()
) {

    @Property(
        name = "Update Branch",
        description = "This will be the branch that Jasper will auto update to. (Recommended to stay on master)",
        type = PropertyType.SELECTOR,
        category = "General",
        options = [ "master", "developer" ],
        subcategory = "General",
        triggerActionOnInitialization = true
    )
    var updateBranch = 0

    @Property(
        name = "Hypixel API Key",
        description = "A very important utility needed for multiple features. You can get your API Key from /api new",
        type = PropertyType.TEXT,
        protectedText = true,
        category = "General",
        subcategory = "General",
        triggerActionOnInitialization = true
    )
    var apiKey = ""

    @Property(
        name = "Grandma Wolf Timer",
        description = "A timer for your Grandma Wolf combo. Works best while in dungeons.",
        type = PropertyType.SWITCH,
        category = "General",
        subcategory = "Skyblock",
        triggerActionOnInitialization = true
    )
    var grandmaWolfTimer = false

    /* Dungeons */
    @Property(
        name = "Auto Reparty Selective User",
        description = "A version of Auto Reparty that only reparties a specific user.",
        type = PropertyType.SWITCH,
        category = "Dungeons",
        subcategory = "Reparty"
    )
    var autoreparty = false

    @Property(
        name = "User to Reparty",
        description = "The user that will be repartied. Requires Auto Reparty Selective User to be on. Use spaces in between usernames if you want to reparty multiple users.",
        type = PropertyType.PARAGRAPH,
        category = "Dungeons",
        placeholder = "Levish",
        subcategory = "Reparty"
    )
    var autorepartyuser = "Levish"

    @Property(
        name = "Reparty delay",
        description = "This is the amount of downtime (in seconds) you have until the user will be repartied.",
        type = PropertyType.NUMBER,
        category = "Dungeons",
        subcategory = "Reparty",
        min = 0,
        max = 60
    )
    var repartydelay = 5

    init {
        registerListener("grandmaWolfTimer") { toggle: Boolean ->
            JasperConfigHUD.showGrandmaWolf = toggle
        }
    }
    fun init() {
        initialize()
    }
}