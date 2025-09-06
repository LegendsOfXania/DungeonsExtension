package fr.legendsofxania.dungeons.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveRoomEvent

@Entry(
    "on_player_leave_room_event",
    "Triggered when a player leaves a room",
    Colors.YELLOW,
    "carbon:build-image"
)
/**
 * The `OnPlayerLeaveRoomEventEntry` entry is used to define an event that triggers when a player leaves a room.
 *
 * ## How could this be used?
 *
 * This could be used to execute a sequence when a player leaves a room.
 */
class OnPlayerLeaveRoomEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val room: Ref<RoomInstance> = emptyRef()
) : EventEntry

@EntryListener(OnPlayerLeaveRoomEventEntry::class)
fun onPlayerLeaveRoomEventListener(
    event: AsyncOnPlayerLeaveRoomEvent,
    query: Query<OnPlayerLeaveRoomEventEntry>
) {
    val room = event.room
    query.findWhere { it.room == room }.triggerAllFor(event.player, context())
}