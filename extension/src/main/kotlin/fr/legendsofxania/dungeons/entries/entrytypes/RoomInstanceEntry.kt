package fr.legendsofxania.dungeons.entries.entrytypes

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.ManifestEntry
import com.typewritermc.engine.paper.entry.entries.Var
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import fr.legendsofxania.dungeons.entries.static.RoomArtifact

@Tags("room_instance")
interface RoomInstanceEntry : ManifestEntry {
    @Help("The next room(s) of the dungeon. Leave empty if this is the last room.")
    val children: List<Ref<RoomInstance>>

    @Help("The artifact that contains the room's data.")
    val artifact: Var<Ref<RoomArtifact>>

    @Help("The direction in which the room will be generated.")
    val direction: Var<Direction>
}

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN
}