package fr.legendsofxania.dungeon.entries.static.facts

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import fr.legendsofxania.dungeon.entries.manifest.dungeon.RoomDefinition
import fr.legendsofxania.dungeon.managers.PlayerManager
import fr.legendsofxania.dungeon.utils.toInt
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

@Entry("in_room_fact", "If the player is in a room", Colors.PURPLE, "eos-icons:storage-class")
/**
 * The 'In Room Fact' is a fact that returns 1 if the player has an active room, and 0 if not.
 *
 * If no room is referenced, it will filter based on if any room is active.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * With this fact, it is possible to make an entry only take action if the player does not have an active room.
 */
class InRoomFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("When not set, it will filter based on if any room is active.")
    val room: Optional<Ref<RoomDefinition>> = Optional.empty(),
) : ReadableFactEntry, KoinComponent {
    private val playerManager: PlayerManager by inject()

    override fun readSinglePlayer(player: Player): FactData {
        val playerRoom = playerManager.getCurrentRoom(player)

        val isInTargetRoom = if (room.isPresent) {
            val targetRoom = room.get()
            playerRoom?.definition == targetRoom
        } else {
            playerRoom != null
        }

        return FactData(isInTargetRoom.toInt())
    }
}
