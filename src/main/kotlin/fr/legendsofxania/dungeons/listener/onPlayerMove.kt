package fr.legendsofxania.dungeons.listener

import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.event.OnPlayerJoinRoomEvent
import fr.legendsofxania.dungeons.event.OnPlayerLeaveRoomEvent
import fr.legendsofxania.dungeons.manager.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMoveListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.from.x == event.to.x && event.from.y == event.to.y && event.from.z == event.to.z) return

        val player = event.player
        val oldState = PlayerManager.getPlayerState(player)
        val newState = PlayerManager.computeDungeonPlayerState(player)

        if (oldState == newState) return

        if (newState != null && (oldState == null || newState.room != oldState.room)) {
            PlayerManager.playerStates[player] = newState
            server.pluginManager.callEvent(OnPlayerJoinRoomEvent(player, newState.room))
        }

        if (oldState != null && (newState == null || oldState.room != newState.room)) {
            server.pluginManager.callEvent(OnPlayerLeaveRoomEvent(player, oldState.room))
        }
    }
}