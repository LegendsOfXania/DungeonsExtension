package fr.legendsofxania.dungeons

import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.listeners.OnPlayerMoveListener
import org.bukkit.event.player.PlayerMoveEvent

@Singleton
object DungeonInitializer : Initializable {
    override suspend fun initialize() {
        server.pluginManager.registerEvents(OnPlayerMoveListener(), plugin)
    }

    override suspend fun shutdown() {
        PlayerMoveEvent.getHandlerList().unregister(OnPlayerMoveListener())
    }
}