package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.binaryData
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.static.RoomTemplate
import org.bukkit.Location
import org.bukkit.structure.Structure
import java.io.ByteArrayOutputStream

/**
 * Manages the saving and loading of room templates using structure data.
 */
object TemplateManager {
    /**
     * Saves a room structure defined by two corner locations into a RoomTemplate entry.
     *
     * @param corner1 The first corner location of the room.
     * @param corner2 The second corner location of the room.
     * @param entry The RoomTemplate entry where the structure data will be saved.
     */
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

    /**
     * Loads a room structure from a RoomTemplate entry.
     *
     * @param entry The RoomTemplate entry containing the structure data.
     * @return The loaded Structure, or null if loading fails.
     */
    suspend fun loadRoom(entry: Ref<RoomTemplate>): Structure? {
        val data = entry.get()?.binaryData() ?: return null
        val inputStream = data.inputStream()

        return server.structureManager.loadStructure(inputStream)
    }
}