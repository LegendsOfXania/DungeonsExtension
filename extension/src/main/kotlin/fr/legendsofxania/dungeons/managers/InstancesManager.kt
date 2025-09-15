package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Singleton
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.util.*

@Singleton
class InstancesManager {
    private val dungeons = mutableMapOf<UUID, DungeonInstance>()

    /**
     * Starts a new dungeon instance at the specified location.
     *
     * @param dungeon Reference to the dungeon definition.
     * @param location The location where the dungeon instance will be placed.
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
     * Starts a new room instance within the specified dungeon.
     *
     * @param dungeon The dungeon instance where the room will be added.
     * @param room Reference to the room definition.
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
     * Stops and removes the specified dungeon instance.
     *
     * @param instance The dungeon instance to be stopped and removed.
     */
    fun stopDungeon(instance: DungeonInstance) {
        val id = dungeons.entries.find { it.value == instance }?.key ?: return
        val removed = dungeons.remove(id) ?: return

        removed.rooms.clear()
    }

    /**
     * Retrieves a room instance by its room definition reference.
     *
     * @param ref Reference to the room definition.
     * @return The RoomInstance if found, null otherwise.
     */
    fun getInstance(ref: Ref<RoomDefinition>): RoomInstance? {
        return dungeons.values.flatMap { it.rooms }.firstOrNull { it.definition == ref }
    }

    /**
     * Retrieves the room instance that contains the player's current location within the specified dungeon.
     *
     * @param player The player whose location is to be checked.
     * @param dungeon The dungeon instance to search within.
     * @return The RoomInstance if the player is inside a room, null otherwise.
     */
    fun getInstance(player: Player, dungeon: DungeonInstance): RoomInstance? {
        val vec = player.location.toVector()
        return dungeon.rooms.firstOrNull { it.box.contains(vec) }
    }
}

data class DungeonInstance(
    val definition: Ref<DungeonDefinition>,
    val location: Location,
    val rooms: MutableList<RoomInstance>
)

data class RoomInstance(
    val definition: Ref<RoomDefinition>,
    val box: BoundingBox
)
