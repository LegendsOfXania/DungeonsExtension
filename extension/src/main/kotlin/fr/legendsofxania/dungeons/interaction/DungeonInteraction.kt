package fr.legendsofxania.dungeons.interaction

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.failure
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.server
import com.typewritermc.engine.paper.utils.toBukkitLocation
import fr.legendsofxania.dungeons.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeons.managers.*
import org.bukkit.Location
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration

/**
 * Represents a dungeon interaction for a player.
 *
 * @param player The player involved in the interaction.
 * @param context The interaction context.
 * @param priority The priority of the interaction.
 * @param eventTriggers The list of event triggers associated with the interaction.
 * @param dungeon The reference to the dungeon definition.
 */
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

        dungeonLocation?.let { location ->
            dungeonInstance?.let { instance ->
                StructureManager().placeRooms(
                    player,
                    context,
                    instance,
                    dungeon.entry?.child ?: return failure("No child room"),
                    location
                )

                playerManager.setDungeon(player, instance)
            } ?: return failure("Could not find the dungeon instance")
        } ?: return failure("Could not find the instance location")

        val teleportLocation = dungeon.entry?.respawnLocation?.get(player, context)?.toBukkitLocation()
        if (teleportLocation == null || !player.teleport(teleportLocation)) {
            return failure("Could not teleport the player to the dungeon")
        }
        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, dungeon))

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
        dungeonInstance?.let {
            StructureManager().removeRooms(it)
            instancesManager.stopDungeon(it)
        }
        dungeonLocation?.let { WorldManager().stopDungeon(it) }

        server.pluginManager.callEvent(AsyncOnPlayerLeaveDungeonEvent(player, dungeon))

        player.sendMessage("Ending interaction!")
    }

    private fun shouldEnd(): Boolean = playerManager.isInDungeon(player)
}

/**
 * Represents a trigger to start a dungeon interaction.
 *
 * @param priority The priority of the trigger.
 * @param eventTriggers The list of event triggers associated with the dungeon start.
 * @param dungeon The reference to the dungeon definition.
 */
data class DungeonStartTrigger(
    val priority: Int,
    val eventTriggers: List<EventTrigger> = emptyList(),
    val dungeon: Ref<DungeonDefinition>
) : EventTrigger {
    override val id: String = "dungeon.start"
}

/**
 * Represents a trigger to stop a dungeon interaction.
 */
data object DungeonStopTrigger : EventTrigger {
    override val id: String = "dungeon.stop"
}
