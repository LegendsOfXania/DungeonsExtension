package fr.legendsofxania.dungeon.entries.manifest.dungeon

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import fr.legendsofxania.dungeon.entries.static.template.RoomTemplate

@Entry(
    "room_definition",
    "The definition of a dungeon room",
    Colors.YELLOW,
    "tabler:building-arch"
)
@Tags("room_definition")
/**
 * The `Room Definition` entry is used to define a room in a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to define a room in a dungeon, including its next room(s), template, and direction.
 */
class RoomDefinition(
    override val id: String = "",
    override val name: String = "",
    @Help("The next room(s) of the dungeon. Leave empty if this is the last room.")
    val children: List<Ref<RoomDefinition>> = emptyList(),
    @Help("The template that contains the room's data.")
    val template: Var<Ref<RoomTemplate>> = ConstVar(emptyRef()),
    @Help("The direction in which the room will be generated.")
    val direction: Var<Direction> = ConstVar(Direction.NORTH),
) : ManifestEntry

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN
}