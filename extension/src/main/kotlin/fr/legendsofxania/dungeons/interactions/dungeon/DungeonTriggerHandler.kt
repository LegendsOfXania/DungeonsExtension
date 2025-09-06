package fr.legendsofxania.dungeons.interactions.dungeon

import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.interaction.TriggerContinuation
import com.typewritermc.engine.paper.interaction.TriggerHandler

@Singleton
class DungeonTriggerHandler : TriggerHandler {
    override suspend fun trigger(event: Event, currentInteraction: Interaction?): TriggerContinuation {

        if (DungeonStopTrigger in event && currentInteraction is DungeonInteraction) {
            return TriggerContinuation.Multi(
                TriggerContinuation.EndInteraction,
                TriggerContinuation.Append(
                    Event(
                        event.player,
                        currentInteraction.context,
                        currentInteraction.eventTriggers
                    )
                ),
            )
        }

        return tryStartDungeonInteraction(event)
    }

    private fun tryStartDungeonInteraction(
        event: Event
    ): TriggerContinuation {
        val triggers = event.triggers
            .filterIsInstance<DungeonStartTrigger>()

        if (triggers.isEmpty()) return TriggerContinuation.Nothing

        val trigger = triggers.maxBy { it.priority }

        return TriggerContinuation.StartInteraction(
            DungeonInteraction(
                event.context,
                trigger.priority,
                event.player,
                trigger.eventTriggers,
                trigger.dungeon
            )
        )
    }
}