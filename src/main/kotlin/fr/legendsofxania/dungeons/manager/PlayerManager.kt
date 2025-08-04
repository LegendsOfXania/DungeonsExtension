package fr.legendsofxania.dungeons.manager

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import fr.legendsofxania.dungeons.events.OnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.OnPlayerLeaveDungeonEvent
import org.bukkit.Bukkit
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

        Bukkit.getPluginManager().callEvent(OnPlayerJoinDungeonEvent(player, dungeon))
    }

    fun joinRoom(player: Player, room: Ref<RoomInstance>) {
        players[player]?.let {
            players[player] = it.copy(room = room)
        }
    }

    fun leaveDungeon(player: Player, dungeon: Ref<DungeonInstance>) {
        players.remove(player)

        Bukkit.getPluginManager().callEvent(OnPlayerLeaveDungeonEvent(player, dungeon))
    }

    fun getPlayer(player: Player): DungeonPlayer? = players[player]

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