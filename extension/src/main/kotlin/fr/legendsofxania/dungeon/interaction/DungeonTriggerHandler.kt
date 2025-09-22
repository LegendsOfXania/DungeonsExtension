package fr.legendsofxania.dungeon.interaction

import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.engine.paper.entry.entries.Event
import com.typewritermc.engine.paper.interaction.TriggerContinuation
import com.typewritermc.engine.paper.interaction.TriggerHandler

/**
 * The `Dungeon Trigger Handler` is responsible for handling dungeon-related triggers.
 *
 * It listens for `DungeonStartTrigger` to initiate a `DungeonInteraction` and for `DungeonStopTrigger`
 * to terminate an ongoing `DungeonInteraction`.
 *
 * ## How could this be used?
 *
 * This handler can be used to manage the flow of dungeon interactions based on specific triggers,
 * allowing for dynamic engagement with dungeon content.
 */
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
                event.player,
                event.context,
                trigger.priority,
                trigger.eventTriggers,
                trigger.dungeon
            )
        )
    }
}