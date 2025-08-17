package fr.legendsofxania.dungeons.manager

import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import fr.legendsofxania.dungeons.events.*
import org.bukkit.entity.Player

object PlayerManager {
    data class DungeonPlayer(
        val player: Player,
        val dungeon: Ref<DungeonInstance>,
        val room: Ref<RoomInstance>? = null
    )

    private val players = mutableMapOf<Player, DungeonPlayer>()

    fun joinDungeon(player: Player, dungeon: Ref<DungeonInstance>) {
        players[player] = DungeonPlayer(player, dungeon)

        server.pluginManager.callEvent(OnPlayerJoinDungeonEvent(player, dungeon))
    }

    fun joinRoom(player: Player, room: Ref<RoomInstance>) {
        players[player]?.let {
            if (it.room == room) {
                players[player] = it.copy(room = room)
                server.pluginManager.callEvent(OnPlayerJoinRoomEvent(player, room))
            }
        }
    }

    fun leaveRoom(player: Player, room: Ref<RoomInstance>) {
        players[player]?.let {
            if (it.room == room) {
                players[player] = it.copy(room = null)
                server.pluginManager.callEvent(OnPlayerLeaveRoomEvent(player, room))
            }
        }
    }

    fun leaveDungeon(player: Player, dungeon: Ref<DungeonInstance>) {
        players.remove(player)

        server.pluginManager.callEvent(OnPlayerLeaveDungeonEvent(player, dungeon))
    }

    fun isPlayerInDungeon(player: Player, dungeon: Ref<DungeonInstance>): Boolean {
        return players[player]?.dungeon == dungeon
    }

    fun isPlayerInDungeon(player: Player): Boolean {
        return players.containsKey(player)
    }

    fun isPlayerInRoom(player: Player, room: Ref<RoomInstance>): Boolean {
        return players[player]?.room == room
    }
}