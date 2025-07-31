package fr.xania.dungeons.manager

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.utils.launch
import com.typewritermc.engine.paper.entry.AssetManager
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.server
import fr.xania.dungeons.entries.`entry-types`.Direction
import fr.xania.dungeons.entries.manifest.DungeonInstance
import fr.xania.dungeons.entries.manifest.RoomInstance
import fr.xania.dungeons.entries.static.RoomArtifact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.structure.Structure
import org.bukkit.util.Vector
import org.koin.java.KoinJavaComponent
import java.io.File
import java.util.Random

object DungeonManager {

    suspend fun initialize(dungeon: Ref<DungeonInstance>) {
        withContext(Dispatchers.Sync) {
            if (!WorldManager.worldExists()) WorldManager.worldCreate()
        }

        val dungeonEntry = dungeon.entry ?: return
        val baseLocation = WorldManager.startDungeonInstance()
        val placedRooms = mutableMapOf<String, Location>()

        placeRoomRecursively(dungeonEntry.child, placedRooms, baseLocation)
    }

    private suspend fun placeRoomRecursively(
        roomInstance: Ref<RoomInstance>,
        placedRooms: MutableMap<String, Location>,
        currentLocation: Location
    ) {
        val roomEntry = roomInstance.entry ?: return
        val artifact = roomEntry.artifact
        val structure = loadRoom(artifact) ?: return

        val roomSize = getStructureSize(structure)
        val offset = getOffsetFromDirection(roomEntry.direction, roomSize)
        val roomLocation = currentLocation.clone().add(offset)

        Dispatchers.Sync.launch {
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

        for (child in roomEntry.children) {
            placeRoomRecursively(child, placedRooms, roomLocation)
        }
    }

    private suspend fun loadRoom(artifactRef: Ref<RoomArtifact>): Structure? {
        val artifact = artifactRef.entry ?: return null

        val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)
        val binaryData = assetManager.fetchBinaryAsset(artifact) ?: return null

        val tempFile = File.createTempFile("plugins/Typewriter/.temp/${artifact.artifactId}", ".nbt").apply {
            writeBytes(binaryData)
        }

        val structure = server.structureManager.loadStructure(tempFile)

        return structure.also {
            tempFile.delete()
        }
    }

    private fun getStructureSize(structure: Structure): Vector {
        val size = structure.size
        return Vector(size.blockX, size.blockY, size.blockZ)
    }

    private fun getOffsetFromDirection(direction: Direction, size: Vector): Vector {
        return when (direction) {
            Direction.NORTH -> Vector(0.0, 0.0, -size.z)
            Direction.SOUTH -> Vector(0.0, 0.0, size.z)
            Direction.EAST  -> Vector(size.x, 0.0, 0.0)
            Direction.WEST  -> Vector(-size.x, 0.0, 0.0)
            Direction.UP    -> Vector(0.0, size.y, 0.0)
            Direction.DOWN  -> Vector(0.0, -size.y, 0.0)
        }
    }
}
