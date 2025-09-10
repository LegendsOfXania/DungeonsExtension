package fr.legendsofxania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.extension.annotations.WithRotation
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.ManifestEntry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var

@Entry(
    "dungeon_definition",
    "The definition of a dungeon",
    Colors.ORANGE,
    "tabler:building"
)
@Tags("dungeon_definition")
/**
 * The `Dungeon Definition` entry is used to define dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to define a dungeon and the first room(s) of the dungeon.
 */
class DungeonDefinition(
    override val id: String = "",
    override val name: String = "",
    @Help("The first room of the dungeon.")
    val child: Ref<RoomDefinition> = emptyRef(),
    @Help("The location where players will respawn when they die in the dungeon. (If the location is not in the dungeon, the dungeon instance will be stopped.)")
    @WithRotation
    val respawnLocation: Var<Position> = ConstVar(Position.ORIGIN),
) : ManifestEntry