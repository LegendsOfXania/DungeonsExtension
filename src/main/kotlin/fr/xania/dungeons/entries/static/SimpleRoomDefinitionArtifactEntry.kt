package fr.xania.dungeons.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry
import fr.xania.dungeons.content.SimpleContentMode

@Entry(
    "simple_room_definition",
    "The definition of a simple room",
     Colors.ORANGE,
    "pajamas:file-addition"
)
@Tags("simple_room_definition")
class SimpleRoomDefinitionArtifactEntry (
    override val id: String = "",
    override val name: String = "",
    @ContentEditor(SimpleContentMode::class)
    override val artifactId: String = "",
) : ArtifactEntry