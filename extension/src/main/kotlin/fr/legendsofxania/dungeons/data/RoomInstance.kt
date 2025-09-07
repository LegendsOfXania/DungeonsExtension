package fr.legendsofxania.dungeons.data

import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.Location

data class RoomInstance(
    val definition: RoomDefinition,
    val minLocation: Location,
    val maxLocation: Location
)
