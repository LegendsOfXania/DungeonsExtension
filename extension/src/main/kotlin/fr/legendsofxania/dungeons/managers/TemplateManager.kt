package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.binaryData
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.static.RoomTemplate
import org.bukkit.Location
import org.bukkit.structure.Structure
import java.io.ByteArrayOutputStream

object TemplateManager {
    suspend fun saveRoom(
        corner1: Location,
        corner2: Location,
        entry: RoomTemplate
    ) {
        val structure = server.structureManager.createStructure()
        structure.fill(corner1, corner2, true)

        val outputStream = ByteArrayOutputStream()
        server.structureManager.saveStructure(outputStream, structure)

        entry.binaryData(outputStream.toByteArray())
    }

    suspend fun loadRoom(entry: Ref<RoomTemplate>): Structure? {
        val data = entry.get()?.binaryData() ?: return null
        val inputStream = data.inputStream()

        return server.structureManager.loadStructure(inputStream)
    }
}