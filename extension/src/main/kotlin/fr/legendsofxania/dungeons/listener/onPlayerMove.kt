package fr.legendsofxania.dungeons.listener

import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinRoomEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveRoomEvent
import fr.legendsofxania.dungeons.managers.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMoveListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {

        if (event.from.x == event.to.x && event.from.y == event.to.y && event.from.z == event.to.z) return

        val player = event.player
        val oldState = PlayerManager.playerStates[player]
        val newState = PlayerManager.computeDungeonPlayerState(player)

        if (oldState == newState) return

        val oldRoom = oldState?.room
        val newRoom = newState?.room

        when (newState) {
            null -> PlayerManager.playerStates.remove(player)
            else -> PlayerManager.playerStates[player] = newState
        }

        if (oldRoom != newRoom) {
            oldRoom?.let { server.pluginManager.callEvent(AsyncOnPlayerLeaveRoomEvent(player, it)) }
            newRoom?.let { server.pluginManager.callEvent(AsyncOnPlayerJoinRoomEvent(player, it)) }
        }
    }
}