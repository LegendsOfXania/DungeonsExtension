package fr.legendsofxania.dungeons.entries.manifest

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.AudienceFilter
import com.typewritermc.engine.paper.entry.entries.AudienceFilterEntry
import com.typewritermc.engine.paper.entry.entries.Invertible
import fr.legendsofxania.dungeons.managers.PlayerManager.getCurrentRoom
import org.bukkit.entity.Player
import java.util.Optional

@Entry("in_room_audience", "Return if a player is in a room or not.", Colors.GREEN, "tabler:building-arch")
/**
 * The `In Room Audience` entry is used to check if a player is currently in a specific room of a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to apply effects to players who are in a specific room of a dungeon.
 */
class InRoomAudience(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    val room: Optional<Ref<RoomDefinition>> = Optional.empty(),
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override suspend fun display(): AudienceFilter = InRoomAudienceFilter(room, inverted, ref())
}

class InRoomAudienceFilter(
    private val room: Optional<Ref<RoomDefinition>>,
    private val inverted: Boolean,
    ref: Ref<out AudienceFilterEntry>
) : AudienceFilter(ref) {

    override fun filter(player: Player): Boolean {
        val playerRoom = player.getCurrentRoom()

        val isInTargetRoom = if (room.isPresent) {
            val targetRoom = room.get()
            playerRoom?.definition == targetRoom
        } else {
            playerRoom != null
        }

        return if (inverted) {
            !isInTargetRoom
        } else {
            isInTargetRoom
        }
    }
}
