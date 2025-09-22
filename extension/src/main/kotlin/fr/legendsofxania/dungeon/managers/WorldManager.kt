package fr.legendsofxania.dungeon.managers

import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.config
import com.typewritermc.engine.paper.utils.server
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.util.*


/**
 * Manages dungeon world instances by allocating and releasing spatial indexes for dungeon placement.
 *
 * Each dungeon instance is placed in a grid layout within a single world, with each cell in the grid
 * being 1000x1000 blocks. The world is created if it does not already exist.
 */
@Singleton
class WorldManager {
    private var maxIndex = -1
    private val usedIndexes = mutableSetOf<Int>()
    private val freeIndexes = PriorityQueue<Int>()

    private val worldName: String by config(
        "dungeons.worldName",
        "dungeons",
        "The name of the world where instances are generated"
    )

    /**
     * Starts a new dungeon instance by determining its location in the specified world.
     * The world is created if it does not already exist.
     *
     * @return The Location where the dungeon instance starts.
     * @throws IllegalStateException if the world cannot be created.
     */
    suspend fun startDungeon(): Location {
        val world = server.getWorld(worldName) ?: withContext(Dispatchers.Sync) { createWorld() }
        ?: throw IllegalStateException("Could not create world $worldName")
        val index = getNextIndex()

        val x = (index % 100) * 1000
        val z = (index / 100) * 1000

        return Location(world, x.toDouble(), 0.0, z.toDouble())
    }

    /**
     * Stops a dungeon instance by releasing its index based on the provided location.
     *
     * @param location The Location of the dungeon instance to be stopped.
     */
    fun stopDungeon(location: Location) {
        val xIndex = (location.blockX / 1000)
        val zIndex = (location.blockZ / 1000)
        val index = zIndex * 100 + xIndex
        releaseIndex(index)
    }

    /**
     * Retrieves the world where dungeon instances are generated.
     *
     * @return The World object if it exists, null otherwise.
     */
    fun getWorld(): World? {
        return server.getWorld(worldName)
    }

    /**
     * Creates a new flat world with the specified name and settings.
     *
     * @return The created World, or null if creation fails.
     */
    private fun createWorld(): World? {
        return WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("{\"layers\":[],\"biome\":\"minecraft:plains\",\"structures\":{}}")
            .generateStructures(false)
            .createWorld()

    }

    /**
     * Retrieves the next available index for a dungeon instance.
     * If there are no free indexes, it increments the maximum index.
     *
     * @return The next available index.
     */
    private fun getNextIndex(): Int {
        val index = freeIndexes.poll() ?: ++maxIndex
        usedIndexes.add(index)
        return index
    }

    /**
     * Releases an index, making it available for future use.
     * If the released index is the current maximum index, it decrements the maximum index.
     *
     * @param index The index to be released.
     */
    private fun releaseIndex(index: Int) {
        if (usedIndexes.remove(index)) {
            freeIndexes.add(index)
            if (index == maxIndex) {
                maxIndex--
            }
        }
    }
}