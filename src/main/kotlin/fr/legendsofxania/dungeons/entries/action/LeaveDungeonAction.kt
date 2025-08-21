package fr.legendsofxania.dungeons.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance

@Entry(
    "leave_dungeon_action",
    "Leave a dungeon",
    Colors.RED,
    "carbon:build-image"
)
/**
 * The `LeaveDungeonAction` entry is used to leaves a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to remove a player from a dungeon and stopped the interaction.
 *
 */
class LeaveDungeonAction(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The dungeon to leave.")
    val dungeon: Ref<DungeonInstance> = emptyRef(),
) : ActionEntry {
    override fun ActionTrigger.execute() {}
}