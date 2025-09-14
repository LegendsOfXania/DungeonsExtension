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

    fun stopDungeon(instance: DungeonInstance) {
        val id = dungeons.entries.find { it.value == instance }?.key ?: return
        val removed = dungeons.remove(id) ?: return

        removed.rooms.clear()
    }

    fun getInstance(player: Player, dungeon: DungeonInstance): RoomInstance? {
        val vec = player.location.toVector()
        return dungeon.rooms.firstOrNull { it.box.contains(vec) }
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
