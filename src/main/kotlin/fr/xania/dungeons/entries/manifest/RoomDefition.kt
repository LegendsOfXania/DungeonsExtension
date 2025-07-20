package fr.xania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry
import fr.xania.dungeons.entries.static.RoomArtifact

@Entry(
    "room_definition",
    "The definition of a dungeon room",
    Colors.YELLOW,
    "tabler:building-arch"
)
@Tags("room_defition")
/**
 * The `RoomDefinition` entry is used to define a room in a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to define a room in a dungeon, including its next room(s), artifact, and direction.
 */
class RoomDefinition (
    override val id: String = "",
    override val name: String = "",
    @Help("The next room(s) of the dungeon. Leave empty if this is the last room.")
    val children: List<Ref<RoomDefinition>> = emptyList(),
    @Help("The artifact that contains the room's data.")
    val artifact: Ref<RoomArtifact> = emptyRef(),
    @Help("The direction in which the room will be generated.")
    val direction: Direction = Direction.NORTH
) : ManifestEntry

enum class Direction{
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN
}