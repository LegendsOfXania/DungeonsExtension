package fr.legendsofxania.dungeons.managers

import fr.legendsofxania.dungeons.managers.InstancesManager.getRoomFor
import org.bukkit.entity.Player

/**
 * Singleton object to manage players' dungeon and room states.
 */
object PlayerManager {
    private val dungeonPlayer = mutableMapOf<Player, DungeonPlayer>()

    /**
     * Assigns a dungeon instance to the player.
     *
     * @param dungeon The dungeon instance to assign to the player.
     */
    fun Player.setDungeon(dungeon: DungeonInstance) {
        val current = dungeonPlayer[this]
        dungeonPlayer[this] = DungeonPlayer(
            dungeon = dungeon,
            room = current?.room
        )
    }

    /**
     * Updates the current room of the player within their assigned dungeon.
     *
     * @param room The new room instance to set for the player.
     */
    fun Player.setRoom(room: RoomInstance) {
        val current = dungeonPlayer[this]
            ?: return
        dungeonPlayer[this] = current.copy(room = room)
    }

    /**
     * Retrieves the dungeon instance assigned to the player.
     *
     * @return The dungeon instance if the player is in a dungeon, null otherwise.
     */
    fun Player.getDungeon(): DungeonInstance? {
        return dungeonPlayer[this]?.dungeon
    }

    /**
     * Retrieves the current room instance of the player within their assigned dungeon.
     *
     * @return The room instance if the player is in a room, null otherwise.
     */
    fun Player.getCurrentRoom(): RoomInstance? {
        return dungeonPlayer[this]?.room
    }

    /**
     * Computes and returns the room instance the player is currently in based on their location.
     *
     * @return The room instance if the player is in a room, null otherwise.
     */
    fun Player.computeRoom(): RoomInstance? {
        val dungeon = this.getDungeon() ?: return null
        return dungeon.getRoomFor(this)
    }

    /**
     * Removes the player from their assigned dungeon and room.
     */
    fun Player.stopDungeon() {
        dungeonPlayer.remove(this)
    }

    /**
     * Checks if the player is currently in a dungeon.
     *
     * @return True if the player is in a dungeon, false otherwise.
     */
    fun Player.isInDungeon(): Boolean {
        return dungeonPlayer.containsKey(this)
    }

    /**
     * Checks if the player is currently in a room within their assigned dungeon.
     *
     * @return True if the player is in a room, false otherwise.
     */
    fun Player.isInRoom(): Boolean {
        return dungeonPlayer[this]?.room != null
    }
}

/**
 * Data class representing a player's current dungeon and room state.
 *
 * @property dungeon The dungeon instance the player is currently in.
 * @property room The room instance the player is currently in, or null if not in any room.
 */
data class DungeonPlayer(
    val dungeon: DungeonInstance,
    val room: RoomInstance?
)
