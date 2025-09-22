package fr.legendsofxania.dungeon

import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeon.listeners.OnPlayerMoveListener
import org.bukkit.event.player.PlayerMoveEvent

@Singleton
object DungeonInitializer : Initializable {
    override suspend fun initialize() {
        server.pluginManager.registerSuspendingEvents(OnPlayerMoveListener(), plugin)
    }

    override suspend fun shutdown() {
        PlayerMoveEvent.getHandlerList().unregister(OnPlayerMoveListener())
    }
}