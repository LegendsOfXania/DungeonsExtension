package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.Sync
import fr.legendsofxania.dungeons.entries.manifest.Direction
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

object StructureManager {
    suspend fun placeRooms(
        player: Player,
        context: InteractionContext,
        instance: DungeonInstance,
        room: Ref<RoomDefinition>,
        loc: Location
    ) {
        val entry = room.entry ?: return logger.severe("Could not place the room ${room.id}. Entry not found.")
        val template = entry.template
        val structure = TemplateManager.loadRoom(template.get(player, context)) ?: return

        val offset = getOffset(entry.direction.get(player, context), structure.size)
        val location = loc.clone().add(offset)

        withContext(Dispatchers.Sync) {
            structure.place(
                location,
                true,
                StructureRotation.NONE,
                Mirror.NONE,
                1,
                1f,
                Random()
            )
        }

        val minLocation = location.clone()
        val maxLocation = location.clone().add(
            structure.size.blockX - 1.0,
            structure.size.blockY - 1.0,
            structure.size.blockZ - 1.0
        )

        InstancesManager.startRoom(instance, room, minLocation, maxLocation)

        for (child in entry.children) {
            placeRooms(player, context, instance, child, location)
        }
    }

    private fun getOffset(direction: Direction, size: Vector): Vector {
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