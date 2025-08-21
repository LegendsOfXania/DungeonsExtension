package fr.legendsofxania.dungeons.listener

import fr.legendsofxania.dungeons.manager.PlayerManager.computeDungeonPlayerState
import fr.legendsofxania.dungeons.manager.PlayerManager.playerStates
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMoveListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val newState = computeDungeonPlayerState(player)

        if (newState == null) {
            playerStates.remove(player)
            return
        }

        val currentState = playerStates[player]
        if (currentState?.room?.entry?.id != newState.room.entry?.id || currentState?.dungeon?.entry?.id != newState.dungeon.entry?.id) {
            playerStates[player] = newState
        }
    }
}