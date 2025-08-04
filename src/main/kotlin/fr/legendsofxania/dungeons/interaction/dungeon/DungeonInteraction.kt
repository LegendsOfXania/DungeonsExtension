package fr.legendsofxania.dungeons.interaction.dungeon

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.events.OnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.OnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeons.manager.DungeonManager
import fr.legendsofxania.dungeons.manager.PlayerManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.Duration

class DungeonInteraction(
    val player: Player,
    override val context: InteractionContext,
    override val priority: Int,
    val eventTriggers: List<EventTrigger>,
    val dungeon: Ref<DungeonInstance>
) : Interaction {
    override suspend fun initialize(): Result<Unit> {

        DungeonManager.initialize(dungeon)
        PlayerManager.joinDungeon(player, dungeon)

        player.sendMessage("Starting interaction!")

        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {

        player.sendMessage("Ticking interaction!")
        if (shouldEnd()) {
            DungeonStopTrigger.triggerFor(player, context)
        }
    }

    override suspend fun teardown(force: Boolean) {

        PlayerManager.leaveDungeon(player, dungeon)

        player.sendMessage("Ending interaction!")
    }

    private fun shouldEnd(): Boolean = false
}

data class DungeonStartTrigger(
    val priority: Int,
    val eventTriggers: List<EventTrigger> = emptyList(),
    val dungeon: Ref<DungeonInstance> = emptyRef()
) : EventTrigger {
    override val id: String = "dungeon.start"
}

data object DungeonStopTrigger : EventTrigger {
    override val id: String = "dungeon.stop"
}