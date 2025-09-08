package fr.legendsofxania.dungeons.data

import org.bukkit.entity.Player

data class DungeonPlayer(
    val player: Player,
    val dungeon: DungeonInstance,
    val room: RoomInstance
)
