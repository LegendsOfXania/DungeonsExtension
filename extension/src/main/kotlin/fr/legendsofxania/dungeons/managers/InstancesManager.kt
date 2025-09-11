package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.util.*

object InstancesManager {
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

    fun DungeonInstance.getRoomFor(player: Player): RoomInstance? {
        val vec = player.location.toVector()
        return rooms.firstOrNull { it.box.contains(vec) }
    }

    fun stopDungeon(instance: DungeonInstance) {
        val id = dungeons.entries.find { it.value == instance }?.key ?: return
        val instance = dungeons.remove(id) ?: return

        instance.rooms.clear()
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
