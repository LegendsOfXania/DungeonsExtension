package fr.xania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry

@Entry(
    "dungeon_Instance",
    "The Instance of a dungeon",
     Colors.ORANGE,
    "tabler:building"
)
@Tags("dungeon_instance")
/**
 * The `DungeonInstance` entry is used to define dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to define a dungeon and the first room(s) of the dungeon.
 */
class DungeonInstance (
    override val id: String = "",
    override val name: String = "",
    @Help("The first room of the dungeon.")
    val children: Ref<RoomInstance> = emptyRef(),
) : ManifestEntry