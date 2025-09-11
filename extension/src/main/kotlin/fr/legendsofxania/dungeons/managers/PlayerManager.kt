package fr.legendsofxania.dungeons.managers

import fr.legendsofxania.dungeons.managers.InstancesManager.getRoomFor
import org.bukkit.entity.Player

object PlayerManager {
    private val dungeonPlayer = mutableMapOf<Player, DungeonPlayer>()

    fun Player.setDungeon(dungeon: DungeonInstance) {
        val current = dungeonPlayer[this]
        dungeonPlayer[this] = DungeonPlayer(
            dungeon = dungeon,
            room = current?.room
        )
    }

    fun Player.setRoom(room: RoomInstance) {
        val current = dungeonPlayer[this]
            ?: return
        dungeonPlayer[this] = current.copy(room = room)
    }

    fun Player.getDungeon(): DungeonInstance? {
        return dungeonPlayer[this]?.dungeon
    }

    fun Player.getCurrentRoom(): RoomInstance? {
        return dungeonPlayer[this]?.room
    }

    fun Player.computeRoom(): RoomInstance? {
        val dungeon = this.getDungeon() ?: return null
        return dungeon.getRoomFor(this)
    }

    fun Player.stopDungeon() {
        dungeonPlayer.remove(this)
    }

    fun Player.isInDungeon(): Boolean {
        return dungeonPlayer.containsKey(this)
    }

    fun Player.isInRoom(): Boolean {
        return dungeonPlayer[this]?.room != null
    }
}

data class DungeonPlayer(
    val dungeon: DungeonInstance,
    val room: RoomInstance?
)
