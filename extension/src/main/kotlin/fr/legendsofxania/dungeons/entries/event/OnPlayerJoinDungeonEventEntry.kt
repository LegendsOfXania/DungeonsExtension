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
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent

@Entry(
    "on_player_join_dungeon_event",
    "Triggered when a player joins a dungeon",
    Colors.YELLOW,
    "carbon:build-image"
)
/**
 * The `OnPlayerJoinDungeonEventEntry` entry is used to define an event that triggers when a player joins a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to execute a sequence when a player enters a dungeon.
 */
class OnPlayerJoinDungeonEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val dungeon: Ref<DungeonInstance> = emptyRef()
) : EventEntry

@EntryListener(OnPlayerJoinDungeonEventEntry::class)
fun onPlayerJoinDungeonEventListener(
    event: AsyncOnPlayerJoinDungeonEvent,
    query: Query<OnPlayerJoinDungeonEventEntry>
) {
    val dungeon = event.dungeon
    query.findWhere { it.dungeon == dungeon }.triggerAllFor(event.player, context())
}