package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.Sync
import fr.legendsofxania.dungeons.entries.manifest.dungeon.Direction
import fr.legendsofxania.dungeons.entries.manifest.dungeon.RoomDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class StructureManager : KoinComponent {
    private val instancesManager: InstancesManager by inject()

    /**
     * Recursively places rooms in the dungeon instance starting from the given location.
     *
     * @param player The player for whom the rooms are being placed.
     * @param context The interaction context.
     * @param instance The dungeon instance where rooms are being placed.
     * @param room The room definition reference to be placed.
     * @param loc The starting location for placing the room.
     */
    suspend fun placeRooms(
        player: Player,
        context: InteractionContext,
        instance: DungeonInstance,
        room: Ref<RoomDefinition>,
        loc: Location
    ) {
        val entry = room.entry ?: return logger.severe("Could not place the room ${room.id}. Entry not found.")
        val template = entry.template
        val structure = TemplateManager().loadRoom(template.get(player, context)) ?: return

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

        instancesManager.startRoom(instance, room, minLocation, maxLocation)

        for (child in entry.children) {
            placeRooms(player, context, instance, child, location)
        }
    }

    /**
     * Removes all rooms associated with the given dungeon instance by setting their blocks to AIR.
     *
     * @param instance The dungeon instance whose rooms are to be removed.
     */
    suspend fun removeRooms(
        instance: DungeonInstance,
    ) {
        val world = instance.location.world

        val blocks = instance.rooms.flatMap {
            val box = it.box

            val minX = box.minX.toInt()
            val minY = box.minY.toInt()
            val minZ = box.minZ.toInt()
            val maxX = box.maxX.toInt()
            val maxY = box.maxY.toInt()
            val maxZ = box.maxZ.toInt()

            buildList {
                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        for (z in minZ..maxZ) {
                            add(Triple(x, y, z))
                        }
                    }
                }
            }
        }

        withContext(Dispatchers.Sync) {
            blocks.forEach { (x, y, z) ->
                world?.getBlockAt(x, y, z)?.type = Material.AIR
            }
        }
    }

    /**
     * Calculates the offset vector based on the given direction and size.
     *
     * @param direction The direction in which to calculate the offset.
     * @param size The size vector of the structure.
     * @return The calculated offset vector.
     */
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