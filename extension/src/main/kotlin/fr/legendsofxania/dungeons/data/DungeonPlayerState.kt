package fr.legendsofxania.dungeons.data

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance

data class DungeonPlayerState(
    val dungeon: Ref<DungeonInstance>,
    val room: Ref<RoomInstance>
)
