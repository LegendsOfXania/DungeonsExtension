package fr.legendsofxania.dungeons.managers

import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.server
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.util.*

private val worldName: String by config(
    "dungeons.worldName",
    "dungeons",
    "The name of the world where instances are generated"
)

object WorldManager {
    private var maxIndex = -1
    private val usedIndexes = mutableSetOf<Int>()
    private val freeIndexes = PriorityQueue<Int>()

    fun startDungeon(): Location {
        val world = server.getWorld(worldName) ?: createWorld()
        ?: throw IllegalStateException("Could not create world $worldName")
        val index = getNextIndex()

        val x = (index % 100) * 1000
        val z = (index / 100) * 1000

        return Location(world, x.toDouble(), 0.0, z.toDouble())
    }

    fun stopDungeon(location: Location) {
        val xIndex = (location.blockX / 1000)
        val zIndex = (location.blockZ / 1000)
        val index = zIndex * 100 + xIndex
        releaseIndex(index)
    }

    private fun createWorld(): World? {
        return WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[],\"biome\":\"minecraft:plains\",\"structures\":{}}")
            .generateStructures(false)
            .createWorld()

    }

    private fun getNextIndex(): Int {
        val index = freeIndexes.poll() ?: ++maxIndex
        usedIndexes.add(index)
        return index
    }

    private fun releaseIndex(index: Int) {
        if (usedIndexes.remove(index)) {
            freeIndexes.add(index)
            if (index == maxIndex) {
                maxIndex--
            }
        }
    }
}