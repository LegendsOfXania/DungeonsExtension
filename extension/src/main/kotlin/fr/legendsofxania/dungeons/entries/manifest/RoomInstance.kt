package fr.legendsofxania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import fr.legendsofxania.dungeons.entries.entrytypes.Direction
import fr.legendsofxania.dungeons.entries.entrytypes.RoomInstanceEntry
import fr.legendsofxania.dungeons.entries.static.RoomArtifact

@Entry(
    "room_instance",
    "The Instance of a dungeon room",
    Colors.YELLOW,
    "tabler:building-arch"
)
@Tags("room_instance")
/**
 * The `RoomInstance` entry is used to define a room in a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to define a room in a dungeon, including its next room(s), artifact, and direction.
 */
class RoomInstance(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<RoomInstance>> = emptyList(),
    override val artifact: Var<Ref<RoomArtifact>> = ConstVar(emptyRef()),
    override val direction: Var<Direction> = ConstVar(Direction.NORTH),
) : RoomInstanceEntry

