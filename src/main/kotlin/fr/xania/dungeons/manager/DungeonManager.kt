package fr.xania.dungeons.manager

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.utils.launch
import com.typewritermc.engine.paper.entry.AssetManager
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.server
import fr.xania.dungeons.entries.manifest.DungeonInstance
import fr.xania.dungeons.entries.manifest.RoomInstance
import fr.xania.dungeons.entries.static.RoomArtifact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.block.structure.Mirror
import org.bukkit.structure.Structure
import org.koin.java.KoinJavaComponent
import java.io.File
import java.util.Random

object DungeonManager {
    suspend fun initialize(dungeon: Ref<DungeonInstance>) {
        withContext(Dispatchers.Sync) {
            if (!WorldManager.worldExists()) WorldManager.worldCreate()
        }

        val dungeonEntry = dungeon.entry?.children ?: return

        placeFirstRoom(dungeonEntry)
    }

    private suspend fun placeFirstRoom(roomInstance: Ref<RoomInstance>) {

        val roomArtifact = roomInstance.entry!!.artifact
        val roomEntry = roomInstance.entry ?: return

        val structure = loadRoom(roomArtifact)!!
        val location = WorldManager.startDungeonInstance()
        val direction = roomEntry.direction
        val rotation = roomEntry.rotation

        Dispatchers.Sync.launch {
            structure.place(
                location,
                true,
                rotation,
                Mirror.NONE,
                0,
                1f,
                Random()
            )
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
}