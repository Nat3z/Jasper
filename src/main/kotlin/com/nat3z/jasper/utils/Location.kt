package com.nat3z.jasper.utils

enum class Location constructor(private val locationName: String) {
    VILLAGE("Village"),
    PRIVATE_ISLAND("Your Island"),
    DUNGEON_HUB("Dungeon Hub"),
    THE_CATACOMBS("The Catacombs"),
    VOID_SEPULTURE("Void Sepulture"),
    THE_END("The End"),
    NONE("None");

    fun asName(): String {
        return locationName
    }

    override fun toString(): String {
        return locationName
    }

    companion object {

        val currentLocation: Location
            get() {
                try {
                    if (SkyUtils.isInSkyblock) {
                        val scoreboard = SkyUtils.scoreboardLines

                        for (s in scoreboard) {
                            val sCleaned = SkyUtils.cleanSB(s)
                            for (location in values()) {
                                if (sCleaned.contains(location.toString()))
                                    return location
                            }
                        }
                    }

                } catch (_: Exception) {

                }

                return NONE
            }
    }
}