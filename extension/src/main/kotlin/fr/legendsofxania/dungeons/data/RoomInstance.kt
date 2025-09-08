package fr.legendsofxania.dungeons.data

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.util.BoundingBox

data class RoomInstance(
    val definition: Ref<RoomDefinition>,
    val box: BoundingBox
)
