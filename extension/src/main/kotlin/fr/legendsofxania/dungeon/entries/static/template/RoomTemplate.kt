package fr.legendsofxania.dungeon.entries.static.template

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry

@Entry(
    "room_template",
    "The template of a room",
    Colors.PINK,
    "tabler:building-bridge-2"
)
@Tags("room_template")
/**
 * The `Room Template` entry is used to store the room's data.
 *
 * ## How could this be used?
 *
 * This could be used to store the room's data and reuse them in a RoomDefinition.
 */

class RoomTemplate(
    override val id: String = "",
    override val name: String = "",
    @ContentEditor(RoomTemplateContentMode::class)
    override val artifactId: String = "",
) : ArtifactEntry