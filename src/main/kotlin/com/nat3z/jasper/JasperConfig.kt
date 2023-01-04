package com.nat3z.jasper

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
        subcategory = "Reparty"
    )
    var repartydelay = 5

    fun init() {
        initialize()
        markDirty()
    }
}