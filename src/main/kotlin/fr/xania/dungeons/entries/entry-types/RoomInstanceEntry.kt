package fr.xania.dungeons.entries.`entry-types`

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry
import fr.xania.dungeons.entries.manifest.RoomInstance
import fr.xania.dungeons.entries.static.RoomArtifact
import org.bukkit.block.structure.StructureRotation

@Tags("room_instance")
interface RoomInstanceEntry : ManifestEntry {
    @Help("The next room(s) of the dungeon. Leave empty if this is the last room.")
    val children: List<Ref<RoomInstance>>
    @Help("The artifact that contains the room's data.")
    val artifact: Ref<RoomArtifact>
    @Help("The direction in which the room will be generated.")
    val direction: Direction
}

enum class Direction{
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN
}