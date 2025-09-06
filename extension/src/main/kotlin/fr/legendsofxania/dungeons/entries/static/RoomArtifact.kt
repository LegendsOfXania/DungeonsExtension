package fr.legendsofxania.dungeons.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry
import fr.legendsofxania.dungeons.interactions.content.RoomContentMode

@Entry(
    "room_artifact",
    "The artifact of a dungeon room",
    Colors.PINK,
    "tabler:building-bridge-2"
)
@Tags("room_artifact")
/**
 * The `Room Artifact` entry is used to store the room's data.
 *
 * ## How could this be used?
 *
 * This could be used to store the room's data for a Room Definition.
 */

class RoomArtifact(
    override val id: String = "",
    override val name: String = "",
    @ContentEditor(RoomContentMode::class)
    override val artifactId: String = "",
) : ArtifactEntry