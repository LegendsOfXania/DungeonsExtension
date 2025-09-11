package fr.legendsofxania.dungeons.listeners

import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinRoomEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveRoomEvent
import fr.legendsofxania.dungeons.managers.PlayerManager.computeRoom
import fr.legendsofxania.dungeons.managers.PlayerManager.getCurrentRoom
import fr.legendsofxania.dungeons.managers.PlayerManager.isInDungeon
import fr.legendsofxania.dungeons.managers.PlayerManager.setRoom
import fr.legendsofxania.dungeons.managers.PlayerManager.stopDungeon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class OnPlayerMoveListener : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (!player.isInDungeon()) return

        if (event.from.blockX == event.to.blockX &&
            event.from.blockY == event.to.blockY &&
            event.from.blockZ == event.to.blockZ
        ) return

        val oldRoom = player.getCurrentRoom()
        val newRoom = player.computeRoom()

        if (oldRoom == newRoom) return

        if (newRoom != null) {
            player.setRoom(newRoom)
        } else {
            player.stopDungeon()
        }

        if (oldRoom != newRoom) {
            oldRoom?.let { player.server.pluginManager.callEvent(AsyncOnPlayerLeaveRoomEvent(player, it.definition)) }
            newRoom?.let { player.server.pluginManager.callEvent(AsyncOnPlayerJoinRoomEvent(player, it.definition)) }
        }
    }
}

