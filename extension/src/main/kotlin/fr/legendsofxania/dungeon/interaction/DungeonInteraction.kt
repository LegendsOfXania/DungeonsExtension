package fr.legendsofxania.dungeon.interaction

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.server
import com.typewritermc.engine.paper.utils.toBukkitLocation
import fr.legendsofxania.dungeon.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeon.events.AsyncOnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeon.events.AsyncOnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeon.managers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        val dungeon = ref.entry ?: return failure("Could not find the dungeon entry")

        dungeonLocation = location
        dungeonInstance = instance

        structureManager.placeRooms(player, context, instance, dungeon.child, location)
        playerManager.setDungeon(player, instance)

        val world = worldManager.getWorld() ?: return failure("Could not find the dungeon world.")
        val coordinate = dungeon.respawnLocation.get(player, context).toBukkitLocation(world)

        withContext(Dispatchers.Sync) { player.teleport(coordinate) }

        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, ref))
        player.msg("Starting interaction!")
        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {
        player.msg("Ticking interaction...")
        if (shouldEnd()) DungeonStopTrigger.triggerFor(player, context)
    }

    override suspend fun teardown(force: Boolean) {
        dungeonInstance?.let {
            structureManager.removeRooms(it)
            instancesManager.stopDungeon(it)
        }

        dungeonLocation?.let { worldManager.stopDungeon(it) }

        server.pluginManager.callEvent(AsyncOnPlayerLeaveDungeonEvent(player, ref))
        player.msg("Ending interaction!")
    }

    private fun shouldEnd() = playerManager.isInDungeon(player)
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
