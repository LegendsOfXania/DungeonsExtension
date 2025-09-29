package fr.legendsofxania.dungeon.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Singleton
import fr.legendsofxania.dungeon.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeon.entries.manifest.dungeon.RoomDefinition
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
     * @param ref Reference to the dungeon definition.
     * @param location The location where the dungeon instance will be placed.
     * @return The created DungeonInstance.
     */
    fun startDungeon(
        ref: Ref<DungeonDefinition>,
        location: Location
    ): DungeonInstance {
        val dungeonInstance = DungeonInstance(
            ref,
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
     * @param ref Reference to the room definition.
     * @param minLocation The minimum corner location of the room's bounding box.
     * @param maxLocation The maximum corner location of the room's bounding box.
     * @return The created RoomInstance.
     */
    fun startRoom(
        dungeon: DungeonInstance,
        ref: Ref<RoomDefinition>,
        minLocation: Location,
        maxLocation: Location
    ): RoomInstance {
        val box = BoundingBox.of(minLocation, maxLocation)
        val roomInstance = RoomInstance(
            ref,
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
        val active = dungeons.values.flatMap { it.rooms }
        return active.firstOrNull { it.definition == ref }
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

    /**
     * Retrieves all active dungeon instances.
     *
     * @return A collection of all DungeonInstance objects.
     */
    fun getInstances(): Collection<DungeonInstance> {
        return dungeons.values
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
