package fr.legendsofxania.dungeons.data

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import org.bukkit.Location

data class DungeonRoomBounds(
    val room: Ref<RoomInstance>,
    val minLocation: Location,
    val maxLocation: Location
)
