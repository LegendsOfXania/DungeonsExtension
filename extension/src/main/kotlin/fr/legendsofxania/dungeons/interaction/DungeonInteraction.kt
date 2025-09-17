package fr.legendsofxania.dungeons.interaction

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.Sync
import com.typewritermc.engine.paper.utils.server
import com.typewritermc.engine.paper.utils.toBukkitLocation
import fr.legendsofxania.dungeons.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeons.managers.*
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
    val dungeon: Ref<DungeonDefinition>
) : Interaction, KoinComponent {
    private val instancesManager: InstancesManager by inject()
    private val playerManager: PlayerManager by inject()
    private var dungeonLocation: Location? = null
    private var dungeonInstance: DungeonInstance? = null

    override suspend fun initialize(): Result<Unit> {
        dungeonLocation = WorldManager().startDungeon()
        dungeonInstance = dungeonLocation?.let { instancesManager.startDungeon(dungeon, it) }

        val instance = dungeonInstance ?: return failure("Could not find the dungeon instance")
        val location = dungeonLocation ?: return failure("Could not find the instance location")
        val room = dungeon.entry?.child ?: return failure("Could not find the first room of the dungeon")

        StructureManager().placeRooms(player, context, instance, room, location)
        playerManager.setDungeon(player, instance)

        val bukkitWorld = WorldManager().getWorld() ?: return failure("Could not find the dungeon world")
        val bukkitLocation = dungeon.entry?.respawnLocation?.get(player, context)?.toBukkitLocation(bukkitWorld)
        if (bukkitLocation == null || !withContext(Dispatchers.Sync) { player.teleport(bukkitLocation) }) {
            return failure("Could not teleport the player to the dungeon")
        }
        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, dungeon))
        player.sendMessage("Starting interaction!")
        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {
        player.sendMessage("Ticking interaction...")
        if (shouldEnd()) DungeonStopTrigger.triggerFor(player, context)
    }

    override suspend fun teardown(force: Boolean) {
        dungeonInstance?.let {
            StructureManager().removeRooms(it)
            instancesManager.stopDungeon(it)
        }
        dungeonLocation?.let { WorldManager().stopDungeon(it) }
        server.pluginManager.callEvent(AsyncOnPlayerLeaveDungeonEvent(player, dungeon))
        player.sendMessage("Ending interaction!")
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
