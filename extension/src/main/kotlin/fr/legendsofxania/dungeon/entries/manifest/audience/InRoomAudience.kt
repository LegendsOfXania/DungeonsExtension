package fr.legendsofxania.dungeon.entries.manifest.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.AudienceFilter
import com.typewritermc.engine.paper.entry.entries.AudienceFilterEntry
import com.typewritermc.engine.paper.entry.entries.Invertible
import fr.legendsofxania.dungeon.entries.manifest.dungeon.RoomDefinition
import fr.legendsofxania.dungeon.managers.PlayerManager
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

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
) : AudienceFilter(ref), KoinComponent {
    private val playerManager: PlayerManager by inject()

    override fun filter(player: Player): Boolean {
        val playerRoom = playerManager.getCurrentRoom(player)

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
