package fr.legendsofxania.dungeons.managers

import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.server
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType

object WorldManager {
    private val worldName: String by config("dungeons.world.name", "dungeons")
    private val baseY: Int by config("dungeons.world.baseY", 0)

    private val usedIndexes = mutableSetOf<Int>()
    private val freeIndexes = mutableListOf<Int>()

    fun startDungeonInstance(): Location {
        val world = server.getWorld(worldName) ?: createWorld()
        val index = getNextIndex()

        val x = (index % 100) * 1000
        val z = (index / 100) * 1000

        return Location(world, x.toDouble(), baseY.toDouble(), z.toDouble())
    }

    fun stopDungeonInstance(location: Location) {
        val index = locationToIndex(location)
        if (usedIndexes.remove(index)) {
            freeIndexes.add(index)
        }
    }

    private fun createWorld(): World {
        return WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[],\"biome\":\"minecraft:plains\",\"structures\":{}}")
            .generateStructures(false)
            .createWorld() ?: error("Failed to create world '$worldName'")
    }

    private fun getNextIndex(): Int {
        val index = if (freeIndexes.isNotEmpty()) {
            freeIndexes.removeFirst()
        } else {
            (usedIndexes.maxOrNull() ?: -1) + 1
        }
        usedIndexes.add(index)
        return index
    }

    private fun locationToIndex(location: Location): Int {
        val xIndex = location.blockX / 1000
        val zIndex = location.blockZ / 1000
        return zIndex * 100 + xIndex
    }
}