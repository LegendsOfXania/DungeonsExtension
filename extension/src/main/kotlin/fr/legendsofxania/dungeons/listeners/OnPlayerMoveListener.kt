package fr.legendsofxania.dungeons.listeners

import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinRoomEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveRoomEvent
import fr.legendsofxania.dungeons.managers.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnPlayerMoveListener : Listener, KoinComponent {
    private val playerManager: PlayerManager by inject()

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.from.blockX == event.to.blockX &&
            event.from.blockY == event.to.blockY &&
            event.from.blockZ == event.to.blockZ
        ) return

        val player = event.player
        val oldRoom = playerManager.getCurrentRoom(player)
        val newRoom = playerManager.computeRoom(player)

        if (!playerManager.isInDungeon(player)) return
        if (oldRoom == newRoom) return

        if (newRoom != null) {
            playerManager.setRoom(player, newRoom)
        } else {
            playerManager.stopDungeon(player)
        }

        if (oldRoom != newRoom) {
            oldRoom?.let { player.server.pluginManager.callEvent(AsyncOnPlayerLeaveRoomEvent(player, it.definition)) }
            newRoom?.let { player.server.pluginManager.callEvent(AsyncOnPlayerJoinRoomEvent(player, it.definition)) }
        }
    }
}

