package fr.legendsofxania.dungeons.data

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import org.bukkit.Location

data class DungeonRoomBounds(
    val room: Ref<RoomInstance>,
    val minLoc: Location,
    val maxLoc: Location
) {
    fun contains(location: Location): Boolean {
        return location.world == minLoc.world &&
                location.x >= minLoc.x && location.x <= maxLoc.x &&
                location.y >= minLoc.y && location.y <= maxLoc.y &&
                location.z >= minLoc.z && location.z <= maxLoc.z
    }
}
