package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.data.DungeonPlayerState
import fr.legendsofxania.dungeons.data.DungeonRoomBounds
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {
    private val dungeonBounds = ConcurrentHashMap<Ref<DungeonInstance>, List<DungeonRoomBounds>>()
    val playerStates = ConcurrentHashMap<Player, DungeonPlayerState>()
    
    fun computeDungeonPlayerState(player: Player): DungeonPlayerState? {
        val loc = player.location
        for ((dungeon, bounds) in dungeonBounds) {
            for (roomBounds in bounds) {
                if (loc.x in roomBounds.minLoc.x..roomBounds.maxLoc.x &&
                    loc.y in roomBounds.minLoc.y..roomBounds.maxLoc.y &&
                    loc.z in roomBounds.minLoc.z..roomBounds.maxLoc.z
                ) {
                    return DungeonPlayerState(dungeon, roomBounds.room)
                }
            }
        }
        return null
    }

    fun setDungeonBounds(dungeon: Ref<DungeonInstance>, bounds: List<DungeonRoomBounds>) {
        dungeonBounds[dungeon] = bounds
    }
}
