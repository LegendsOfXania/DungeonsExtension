package fr.legendsofxania.dungeons.managers

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent
import org.bukkit.entity.Player

object InstancesManager {
    fun startDungeon(
        player: Player,
        dungeon: Ref<DungeonDefinition>
    ) {

        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, dungeon))
    }
}