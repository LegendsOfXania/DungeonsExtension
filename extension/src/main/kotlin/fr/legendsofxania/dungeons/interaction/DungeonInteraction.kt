package fr.legendsofxania.dungeons.interaction

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeons.managers.*
import org.bukkit.Location
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration

class DungeonInteraction(
    val player: Player,
    override val context: InteractionContext,
    override val priority: Int,
    val eventTriggers: List<EventTrigger>,
    val ref: Ref<DungeonDefinition>
) : Interaction, KoinComponent {
    private var dungeonLocation: Location? = null
    private var dungeonInstance: DungeonInstance? = null

    private val worldManager: WorldManager by inject()
    private val structureManager: StructureManager by inject()
    private val playerManager: PlayerManager by inject()
    private val instancesManager: InstancesManager by inject()

    override suspend fun initialize(): Result<Unit> {
        val location = worldManager.startDungeon()
        val instance = instancesManager.startDungeon(ref, location)
        val room = ref.entry ?: return failure("Could not find the dungeon entry")

        dungeonLocation = location
        dungeonInstance = instance

        structureManager.placeRooms(player, context, instance, room.child, location)
        playerManager.setDungeon(player, instance)

        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, ref))
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
        server.pluginManager.callEvent(AsyncOnPlayerLeaveDungeonEvent(player, ref))
        player.sendMessage("Ending interaction!")
    }

    private fun shouldEnd(): Boolean = false
}

data class DungeonStartTrigger(
    val priority: Int,
    val eventTriggers: List<EventTrigger> = emptyList(),
    val dungeon: Ref<DungeonDefinition>
) : EventTrigger {
    override val id = "dungeon.start"
}

data object DungeonStopTrigger : EventTrigger {
    override val id = "dungeon.stop"
}
