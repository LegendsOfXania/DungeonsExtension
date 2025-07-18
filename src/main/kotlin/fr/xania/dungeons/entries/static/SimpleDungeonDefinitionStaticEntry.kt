package fr.xania.dungeons.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.StaticEntry

@Entry(
    "simple_dungeon_definition",
    "The definition of a simple dungeon",
     Colors.ORANGE,
    "tabler:building-arch"
)
class SimpleDungeonDefinitionStaticEntry (
    override val id: String = "",
    override val name: String = "",
    @Help("The first room of the dungeon.")
    val room: List<Ref<SimpleRoomDefinitionArtifactEntry>> = emptyList()
) : StaticEntry