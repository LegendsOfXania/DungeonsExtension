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
        val from = event.from
        val to = event.to

        if (from.x == to.x && from.y == to.y && from.z == to.z) return

        val player = event.player
        val oldState = PlayerManager.getPlayerState(player)
        val newState = PlayerManager.computeDungeonPlayerState(player)

        if (oldState == newState) return

        val oldRoom = oldState?.room
        val newRoom = newState?.room

        when (newState) {
            null -> PlayerManager.playerStates.remove(player)
            else -> PlayerManager.playerStates[player] = newState
        }

        if (oldRoom != newRoom) {
            oldRoom?.let { server.pluginManager.callEvent(OnPlayerLeaveRoomEvent(player, it)) }
            newRoom?.let { server.pluginManager.callEvent(OnPlayerJoinRoomEvent(player, it)) }
        }
    }
}