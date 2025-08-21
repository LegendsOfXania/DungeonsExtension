package fr.legendsofxania.dungeons.manager

import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.server
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType

object WorldManager {
    val worldName: String by config("dungeons.world.name", "dungeons", "The name of the world where dungeons will be generated")
    val baseY: Int by config("dungeons.world.baseY", 0, "The base Y level for dungeon instances in the dungeons world")

    private val usedInstances = mutableSetOf<Int>()
    private val availableIndexes = mutableListOf<Int>()

    fun worldCreate(): World? {
        if (worldExists()) return server.getWorld(worldName)

        val creator = WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[],\"biome\":\"minecraft:plains\",\"structures\":{}}")
            .generateStructures(false)
            .environment(World.Environment.NORMAL)

        return creator.createWorld().also {
            logger.info("World \"$worldName\" created successfully.")
        }
    }

    fun worldExists(): Boolean = server.getWorld(worldName) != null

    fun startDungeonInstance(): Location {


        val world = server.getWorld(worldName) ?: throw IllegalStateException("World '$worldName' does not exist.")

        val index = getAvailableIndex()
        usedInstances.add(index)

        val x = (index % 100) * 1000
        val z = (index / 100) * 1000

        val location = Location(world, x.toDouble(), baseY.toDouble(), z.toDouble())

        return location
    }

    fun stopDungeonInstance(location: Location) {
        val xIndex = location.blockX / 1000
        val zIndex = location.blockZ / 1000
        val index = zIndex * 100 + xIndex

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