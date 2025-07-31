package fr.legendsofxania.dungeons.entries.entrytypes

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance

@Tags("dungeon_instance")
interface DungeonInstanceEntry : ManifestEntry {
    @Help("The first room of the dungeon.")
    val child: Ref<RoomInstance>
}