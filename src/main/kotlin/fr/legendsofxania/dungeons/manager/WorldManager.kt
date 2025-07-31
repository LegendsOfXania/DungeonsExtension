package fr.legendsofxania.dungeons.manager

import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.config
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType

object WorldManager {
    val worldName: String by config("dungeons.world.name", "dungeons", "The name of the world where dungeons will be generated")
    val instanceSpacing: Int by config("dungeons.world.instanceSpacing", 1000, "The spacing between dungeon instances in the dungeons world")
    val gridWidth: Int by config("dungeons.world.gridWidth", 30, "The number of instances per row in the grid")
    val baseY: Int by config("dungeons.world.baseY", 0, "The base Y level for dungeon instances in the dungeons world")

    private val usedInstances = mutableSetOf<Int>()
    private val availableIndexes = mutableListOf<Int>()

    fun worldCreate(): World? {
        if (worldExists()) return Bukkit.getWorld(worldName)

        val creator = WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[],\"biome\":\"minecraft:plains\",\"structures\":{}}")
            .generateStructures(false)
            .environment(World.Environment.NORMAL)

        return creator.createWorld().also {
            logger.info("World \"$worldName\" created successfully.")
        }
    }

    fun worldExists(): Boolean = Bukkit.getWorld(worldName) != null

    fun startDungeonInstance(): Location {
        val world = Bukkit.getWorld(worldName) ?: throw IllegalStateException("World '$worldName' does not exist.")

        val index = getAvailableIndex()
        usedInstances.add(index)

        val x = (index % gridWidth) * instanceSpacing
        val z = (index / gridWidth) * instanceSpacing

        val location = Location(world, x.toDouble(), baseY.toDouble(), z.toDouble())

        return location
    }

    fun stopDungeonInstance(location: Location) {
        val xIndex = location.blockX / instanceSpacing
        val zIndex = location.blockZ / instanceSpacing
        val index = zIndex * gridWidth + xIndex

        if (usedInstances.remove(index)) {
            availableIndexes.add(index)
        }
    }

    private fun getAvailableIndex(): Int {
        return if (availableIndexes.isNotEmpty()) {
            availableIndexes.removeFirst()
        } else {
            (usedInstances.maxOrNull() ?: -1) + 1
        }
    }
}