package fr.legendsofxania.dungeons.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.entries.priority
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.interactions.dungeon.DungeonStartTrigger

@Entry(
    "start_dungeon_action",
    "Start a dungeon",
    Colors.RED,
    "carbon:build-image"
)
/**
 * The `StartDungeonAction` entry is used to start a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to start a dungeon for a player, initializing the dungeon's state
 * and preparing him for the adventure.
 */
class StartDungeonAction(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The dungeon to start.")
    val dungeon: Ref<DungeonInstance> = emptyRef(),
) : ActionEntry {
    override val eventTriggers: List<EventTrigger>
        get() = listOf(
            DungeonStartTrigger(
                this.priority,
                super.eventTriggers,
                dungeon
            )
        )

    override fun ActionTrigger.execute() {}
}