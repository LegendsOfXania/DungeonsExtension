package fr.legendsofxania.dungeons.interactions.dungeon

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.server
import fr.legendsofxania.dungeons.data.DungeonRoomBounds
import fr.legendsofxania.dungeons.entries.manifest.DungeonInstance
import fr.legendsofxania.dungeons.events.AsyncOnPlayerJoinDungeonEvent
import fr.legendsofxania.dungeons.events.AsyncOnPlayerLeaveDungeonEvent
import fr.legendsofxania.dungeons.managers.PlayerManager
import fr.legendsofxania.dungeons.managers.StructureManager
import fr.legendsofxania.dungeons.managers.WorldManager
import org.bukkit.Location
import org.bukkit.entity.Player
import java.time.Duration

class DungeonInteraction(
    override val context: InteractionContext,
    override val priority: Int,
    val player: Player,
    val eventTriggers: List<EventTrigger>,
    val dungeon: Ref<DungeonInstance>
) : Interaction {
    private lateinit var location: Location

    override suspend fun initialize(): Result<Unit> {
        location = WorldManager.startDungeonInstance()

        val dungeonEntry = dungeon.entry ?: return Result.failure(IllegalArgumentException("DungeonInstance entry not found"))
        val bounds = mutableListOf<DungeonRoomBounds>()
        val placedRooms = mutableMapOf<String, Location>()

        StructureManager.setupRooms(player, context, dungeonEntry.child, placedRooms, location, bounds)
        PlayerManager.setDungeonBounds(dungeon, bounds.toList())

        server.pluginManager.callEvent(AsyncOnPlayerJoinDungeonEvent(player, dungeon))

        player.sendMessage("[DEBUG] Starting interaction!")

        return ok(Unit)
    }

    override suspend fun tick(deltaTime: Duration) {
        player.sendMessage("[DEBUG] Ticking interaction!")
        if (shouldEnd()) {
            DungeonStopTrigger.triggerFor(player, context)
        }
    }

    override suspend fun teardown(force: Boolean) {
        val bounds = PlayerManager.getDungeonBounds(dungeon)
        if (bounds != null) StructureManager.removeRooms(bounds)

        WorldManager.stopDungeonInstance(location)

        server.pluginManager.callEvent(AsyncOnPlayerLeaveDungeonEvent(player, dungeon))

        player.sendMessage("[DEBUG] Ending interaction!")
    }

    private fun shouldEnd(): Boolean =
        PlayerManager.playerStates[player] == null
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