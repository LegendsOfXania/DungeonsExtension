package fr.legendsofxania.dungeons.interaction

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import fr.legendsofxania.dungeons.data.DungeonInstance
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.managers.InstancesManager
import fr.legendsofxania.dungeons.managers.StructureManager
import fr.legendsofxania.dungeons.managers.WorldManager
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

class DungeonInteraction(
    val player: Player,
    override val context: InteractionContext,
    override val priority: Int,
    val eventTriggers: List<EventTrigger>,
    val dungeon: Ref<DungeonDefinition>
) : Interaction {
    private var dungeonLocation: Location? = null
    private var dungeonInstance: DungeonInstance? = null

    override suspend fun initialize(): Result<Unit> {
        dungeonLocation = WorldManager.startDungeon()
        dungeonInstance = dungeonLocation?.let { InstancesManager.startDungeon(dungeon, it) }

        dungeonLocation?.let { loc ->
            dungeonInstance?.let { instance ->
                StructureManager.placeRooms(
                    player,
                    context,
                    instance,
                    dungeon.entry?.child ?: return failure("No child room"),
                    loc
                )
            } ?: return failure("Could not find the dungeon instance")
        } ?: return failure("Could not find the instance location")

        player.sendMessage("Starting interaction!")

        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {
        player.sendMessage("Ticking interaction...")

        if (shouldEnd()) {
            DungeonStopTrigger.triggerFor(player, context)
        }
    }

    override suspend fun teardown(force: Boolean) {
        dungeonLocation?.let { WorldManager.stopDungeon(it) }
        dungeonInstance?.let { InstancesManager.stopDungeon(it) }

        player.sendMessage("Ending interaction!")
    }

    private fun shouldEnd(): Boolean = false
}

data class DungeonStartTrigger(
    val priority: Int,
    val eventTriggers: List<EventTrigger> = emptyList(),
    val dungeon: Ref<DungeonDefinition>
) : EventTrigger {
    override val id: String = "dungeon.start"
}

data object DungeonStopTrigger : EventTrigger {
    override val id: String = "dungeon.stop"
}
