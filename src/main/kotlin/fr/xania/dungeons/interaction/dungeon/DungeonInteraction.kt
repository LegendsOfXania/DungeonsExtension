package fr.xania.dungeons.interaction.dungeon

import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import fr.xania.dungeons.manager.WorldManager
import org.bukkit.entity.Player
import java.time.Duration

class DungeonInteraction(
    val player: Player,
    override val context: InteractionContext,
    override val priority: Int,
    val eventTriggers: List<EventTrigger>
) : Interaction {
    override suspend fun initialize(): Result<Unit> {

        // TODO: Initialize the dungeon interaction, e.g., load world, place rooms, etc.

        player.sendMessage("Starting interaction!")

        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {

        player.sendMessage("Ticking interaction! Delta time: ${deltaTime.toMillis()} ms")
        if (shouldEnd()) {
            DungeonStopTrigger.triggerFor(player, context)
        }
    }

    override suspend fun teardown(force: Boolean) {

        player.sendMessage("Ending interaction!")
    }

    private fun shouldEnd(): Boolean = false // Your end condition
}

data class DungeonStartTrigger(
    val priority: Int,
    val eventTriggers: List<EventTrigger> = emptyList()
) : EventTrigger {
    override val id: String = "dungeon.start"
}

data object DungeonStopTrigger : EventTrigger {
    override val id: String = "dungeon.stop"
}