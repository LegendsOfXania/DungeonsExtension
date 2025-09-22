package fr.legendsofxania.dungeon.listeners

import com.typewritermc.core.utils.UntickedAsync
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeon.events.AsyncOnPlayerJoinRoomEvent
import fr.legendsofxania.dungeon.events.AsyncOnPlayerLeaveRoomEvent
import fr.legendsofxania.dungeon.managers.PlayerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnPlayerMoveListener : Listener, KoinComponent {
    private val playerManager: PlayerManager by inject()

    @EventHandler
    suspend fun onPlayerMove(event: PlayerMoveEvent) {
        val from = event.from
        val to = event.to

        if (from.blockX == to.blockX &&
            from.blockY == to.blockY &&
            from.blockZ == to.blockZ
        ) return

        val player = event.player

        if (!playerManager.isInDungeon(player)) return

        val oldRoom = playerManager.getCurrentRoom(player)
        val newRoom = playerManager.computeRoom(player)

        if (oldRoom == newRoom) return

        withContext(Dispatchers.UntickedAsync) {
            if (newRoom != null) {
                playerManager.setRoom(player, newRoom)
            } else {
                playerManager.stopDungeon(player)
            }

            if (oldRoom != newRoom) {
                oldRoom?.let { server.pluginManager.callEvent(AsyncOnPlayerLeaveRoomEvent(player, it.definition)) }
                newRoom?.let { server.pluginManager.callEvent(AsyncOnPlayerJoinRoomEvent(player, it.definition)) }
            }

        }
    }
}

