package fr.xania.dungeons.manager

import com.typewritermc.engine.paper.logger
import com.typewritermc.engine.paper.utils.config
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.io.File

object WorldManager {
    val worldName: String by config("dungeon", "dungeons", "The name of the world where dungeons will be generated")

    fun worldCreate(): World? {
        if (worldExists()) return Bukkit.getWorld(worldName)

        val creator = WorldCreator(worldName)
            .type(WorldType.FLAT)
            .generatorSettings("minecraft:air")
            .generateStructures(false)
            .environment(World.Environment.NORMAL)

        return creator.createWorld().also {
            logger.info("World \"$worldName\" created successfully.")
        }
    }

    fun worldLoad(): World? {
        if (Bukkit.getWorld(worldName) != null) return Bukkit.getWorld(worldName)

        val worldFolder = File(Bukkit.getWorldContainer(), worldName)
        if (!worldFolder.exists() || !File(worldFolder, "level.dat").exists()) return null

        return WorldCreator(worldName).createWorld()?.also {
            logger.info("World \"$worldName\" loaded successfully.")
        }
    }

    fun worldExists(): Boolean = Bukkit.getWorld(worldName) != null

    fun worldIsLoaded(): Boolean = Bukkit.getWorld(worldName) != null

    fun worldHasPlayers(): Boolean = Bukkit.getWorld(worldName)?.players?.isNotEmpty() == true

    fun worldUnLoad(): Boolean = Bukkit.unloadWorld(worldName, true)
}