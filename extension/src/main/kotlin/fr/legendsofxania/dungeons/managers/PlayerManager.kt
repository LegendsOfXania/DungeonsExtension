package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.extension.annotations.Singleton
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Singleton
class PlayerManager : KoinComponent {
    private val instancesManager: InstancesManager by inject()
    private val dungeonPlayer = mutableMapOf<Player, DungeonPlayer>()

    /**
     * Assigns a dungeon instance to the player.
     *
     * @param dungeon The dungeon instance to assign to the player.
     */
    fun setDungeon(player: Player, dungeon: DungeonInstance) {
        val current = dungeonPlayer[player]
        dungeonPlayer[player] = DungeonPlayer(
            dungeon = dungeon,
            room = current?.room
        )
    }

    /**
     * Updates the current room of the player within their assigned dungeon.
     *
     * @param room The new room instance to set for the player.
     */
    fun setRoom(player: Player, room: RoomInstance) {
        val current = dungeonPlayer[player]
            ?: return
        dungeonPlayer[player] = current.copy(room = room)
    }

    /**
     * Retrieves the dungeon instance assigned to the player.
     *
     * @return The dungeon instance if the player is in a dungeon, null otherwise.
     */
    fun getDungeon(player: Player): DungeonInstance? {
        return dungeonPlayer[player]?.dungeon
    }

    /**
     * Retrieves the current room instance of the player within their assigned dungeon.
     *
     * @return The room instance if the player is in a room, null otherwise.
     */
    fun getCurrentRoom(player: Player): RoomInstance? {
        return dungeonPlayer[player]?.room
    }

    /**
     * Computes and returns the room instance the player is currently in based on their location.
     *
     * @return The room instance if the player is in a room, null otherwise.
     */
    fun computeRoom(player: Player): RoomInstance? {
        val dungeon = getDungeon(player) ?: return null
        return instancesManager.getInstance(player, dungeon)
    }

    /**
     * Removes the player from their assigned dungeon and room.
     */
    fun stopDungeon(player: Player) {
        dungeonPlayer.remove(player)
    }

    /**
     * Checks if the player is currently in a dungeon.
     *
     * @return True if the player is in a dungeon, false otherwise.
     */
    fun isInDungeon(player: Player): Boolean {
        return dungeonPlayer.containsKey(player)
    }

    /**
     * Checks if the player is currently in a room within their assigned dungeon.
     *
     * @return True if the player is in a room, false otherwise.
     */
    fun isInRoom(player: Player): Boolean {
        return dungeonPlayer[player]?.room != null
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
