package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.data.DungeonInstance
import fr.legendsofxania.dungeons.data.RoomInstance
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.Location
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

    fun stopDungeon(instance: DungeonInstance) {
        val id = dungeons.entries.find { it.value == instance }?. key ?: return
        val instance = dungeons.remove(id) ?: return

        instance.rooms.clear()
    }

    fun startRoom(
        dungeon: DungeonInstance,
        room: Ref<RoomDefinition>,
        minLocation: Location,
        maxLocation: Location
    ) {
        val box = BoundingBox.of(minLocation, maxLocation)
        val roomInstance = RoomInstance(
            room,
            box
        )

        dungeon.rooms.add(roomInstance)
    }
}