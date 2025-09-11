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
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveDungeonEvent

@Entry(
    "on_player_leave_dungeon_event",
    "Triggered when a player leaves a dungeon",
    Colors.YELLOW,
    "carbon:build-image"
)
/**
 * The `On Player Leave Dungeon Event Entry` entry is used to define an event that triggers when a player leaves a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to execute a sequence when a player enters a dungeon.
 */
class OnPlayerLeaveDungeonEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val dungeon: Ref<DungeonDefinition> = emptyRef()
) : EventEntry

@EntryListener(OnPlayerLeaveDungeonEventEntry::class)
fun onPlayerLeaveDungeonEventListener(
    event: AsyncOnPlayerLeaveDungeonEvent,
    query: Query<OnPlayerLeaveDungeonEventEntry>
) {
    val dungeon = event.dungeon
    query.findWhere { it.dungeon == dungeon }.triggerAllFor(event.player, context())
}