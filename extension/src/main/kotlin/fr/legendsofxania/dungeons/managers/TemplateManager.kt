package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry
import com.typewritermc.engine.paper.entry.entries.binaryData
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.server
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.structure.Structure
import java.io.ByteArrayOutputStream

object TemplateManager {
    suspend fun saveRoom(player: Player, corner1: Location, corner2: Location, entry: ArtifactEntry) {
        val structure = server.structureManager.createStructure()
        structure.fill(corner1, corner2, true)

        val outputStream = ByteArrayOutputStream()
        server.structureManager.saveStructure(outputStream, structure)

        entry.binaryData(outputStream.toByteArray())

        player.msg("Room Template saved successfully!")
    }

    suspend fun loadRoom(entry: Ref<ArtifactEntry>): Structure? {
        val data = entry.get()?.binaryData() ?: return null
        val inputStream = data.inputStream()

        return server.structureManager.loadStructure(inputStream)
    }
}