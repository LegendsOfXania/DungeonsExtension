package fr.legendsofxania.dungeons.data

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import org.bukkit.Location

data class DungeonInstance(
    val definition: Ref<DungeonDefinition>,
    val location: Location,
    val rooms: MutableList<RoomInstance>
)
