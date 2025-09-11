package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.util.*

/**
 * Manages dungeon and room instances.
 */
object InstancesManager {
    private val dungeons = mutableMapOf<UUID, DungeonInstance>()

    /**
     * Start a new dungeon instance at the specified location.
     *
     * @param dungeon The dungeon definition reference.
     * @param location The location where the dungeon instance will be started.
     * @return The created DungeonInstance.
     */
    fun startDungeon(
        dungeon: Ref<DungeonDefinition>,
        location: Location
    ): DungeonInstance {
        val dungeonInstance = DungeonInstance(
            dungeon,
            location,
            mutableListOf()
        )
        val id = UUID.randomUUID()

        dungeons[id] = dungeonInstance

        return dungeonInstance
    }

    /**
     * Start a new room instance within a dungeon.
     *
     * @param dungeon The dungeon instance where the room will be added.
     * @param room The room definition reference.
     * @param minLocation The minimum corner location of the room's bounding box.
     * @param maxLocation The maximum corner location of the room's bounding box.
     * @return The created RoomInstance.
     */
    fun startRoom(
        dungeon: DungeonInstance,
        room: Ref<RoomDefinition>,
        minLocation: Location,
        maxLocation: Location
    ): RoomInstance {
        val box = BoundingBox.of(minLocation, maxLocation)
        val roomInstance = RoomInstance(
            room,
            box
        )

        dungeon.rooms.add(roomInstance)

        return roomInstance
    }

    /**
     * Get the room instance for a player based on their current location.
     *
     * @receiver DungeonInstance The dungeon instance to search within.
     * @param player The player whose location will be checked.
     * @return The RoomInstance the player is currently in, or null if not in any room.
     */
    fun DungeonInstance.getRoomFor(player: Player): RoomInstance? {
        val vec = player.location.toVector()
        return rooms.firstOrNull { it.box.contains(vec) }
    }

    /**
     * Stop and remove a dungeon instance.
     *
     * @param instance The dungeon instance to be stopped.
     */
    fun stopDungeon(instance: DungeonInstance) {
        val id = dungeons.entries.find { it.value == instance }?.key ?: return
        val instance = dungeons.remove(id) ?: return

        instance.rooms.clear()
    }
}

/**
 * Data class representing a dungeon instance.
 *
 * @property definition Reference to the dungeon definition.
 * @property location The location where the dungeon instance is placed.
 * @property rooms List of room instances within the dungeon.
 */
data class DungeonInstance(
    val definition: Ref<DungeonDefinition>,
    val location: Location,
    val rooms: MutableList<RoomInstance>
)

/**
 * Data class representing a room instance within a dungeon.
 *
 * @property definition Reference to the room definition.
 * @property box The bounding box defining the room's area.
 */
data class RoomInstance(
    val definition: Ref<RoomDefinition>,
    val box: BoundingBox
)
