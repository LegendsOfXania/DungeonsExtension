package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.entry.entries.binaryData
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.data.DungeonRoomBounds
import fr.legendsofxania.dungeons.entries.entrytypes.Direction
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import fr.legendsofxania.dungeons.entries.static.RoomArtifact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.entity.Player
import org.bukkit.structure.Structure
import org.bukkit.util.Vector
import java.io.ByteArrayInputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object StructureManager {
    private val structureCache = ConcurrentHashMap<String, Structure>()

    suspend fun setupRooms(
        player: Player,
        context: InteractionContext,
        roomInstance: Ref<RoomInstance>,
        placedRooms: MutableMap<String, Location>,
        currentLocation: Location,
        bound: MutableList<DungeonRoomBounds>
    ) {
        val roomEntry = roomInstance.entry ?: return
        val artifact = roomEntry.artifact
        val structure = loadRoom(artifact.get(player, context)) ?: return

        val roomSize = structure.size
        val offset = getOffsetFromDirection(roomEntry.direction.get(player, context), roomSize)
        val roomLocation = currentLocation.clone().add(offset)

        withContext(Dispatchers.Sync) {
            structure.place(
                roomLocation,
                true,
                StructureRotation.NONE,
                Mirror.NONE,
                0,
                1f,
                Random()
            )
        }

        placedRooms[roomEntry.id] = roomLocation

        val minLoc = roomLocation.clone()
        val maxLoc = roomLocation.clone().add(roomSize.x - 1, roomSize.y - 1, roomSize.z - 1)

        bound.add(DungeonRoomBounds(room = roomInstance, minLoc = minLoc, maxLoc = maxLoc))

        for (child in roomEntry.children) {
            setupRooms(player, context, child, placedRooms, roomLocation, bound)
        }
    }

    private suspend fun loadRoom(ref: Ref<RoomArtifact>): Structure? {
        val artifact = ref.get() ?: return null
        val data = artifact.binaryData() ?: return null

        structureCache[artifact.artifactId]?.let { return it }
        return server.structureManager.loadStructure(ByteArrayInputStream(data))
    }

    private fun getOffsetFromDirection(direction: Direction, size: Vector): Vector {
        return when (direction) {
            Direction.NORTH -> Vector(0.0, 0.0, -size.z)
            Direction.SOUTH -> Vector(0.0, 0.0, size.z)
            Direction.EAST -> Vector(size.x, 0.0, 0.0)
            Direction.WEST -> Vector(-size.x, 0.0, 0.0)
            Direction.UP -> Vector(0.0, size.y, 0.0)
            Direction.DOWN -> Vector(0.0, -size.y, 0.0)
        }
    }
}
