package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.engine.paper.entry.entries.binaryData
import com.typewritermc.engine.paper.entry.entries.hasData
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.static.template.RoomTemplate
import org.bukkit.Location
import org.bukkit.structure.Structure
import java.io.ByteArrayOutputStream

@Singleton
class TemplateManager {
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
     * @param ref The RoomTemplate entry containing the structure data.
     * @return The loaded Structure, or null if loading fails.
     */
    suspend fun loadRoom(ref: Ref<RoomTemplate>): Structure? {
        logger.info("Loading structure for template $ref...")
        val entry = ref.entry ?: return null

        if (entry.hasData()) {
            logger.info("Found structure data for template $ref, loading...")
            val inputStream = entry.binaryData()?.inputStream() ?: return null
            return server.structureManager.loadStructure(inputStream)
        }
        logger.info("No structure data found for template $ref.")
        return null
    }
}