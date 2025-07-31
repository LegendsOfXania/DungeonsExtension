package fr.legendsofxania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import fr.legendsofxania.dungeons.entries.static.RoomArtifact
import fr.legendsofxania.dungeons.entries.entrytypes.Direction
import fr.legendsofxania.dungeons.entries.entrytypes.RoomInstanceEntry

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
class RoomInstance (
    override val id: String = "",
    override val name: String = "",
    @Help("The next room(s) of the dungeon. Leave empty if this is the last room.")
    override val children: List<Ref<RoomInstance>> = emptyList(),
    @Help("The artifact that contains the room's data.")
    override val artifact: Ref<RoomArtifact> = emptyRef(),
    @Help("The direction in which the room will be generated.")
    override val direction: Direction = Direction.NORTH,
) : RoomInstanceEntry

