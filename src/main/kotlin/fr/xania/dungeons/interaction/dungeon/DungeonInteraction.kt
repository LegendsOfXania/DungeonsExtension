package fr.xania.dungeons.interaction.dungeon

import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.interaction.Interaction
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.entry.AssetManager
import com.typewritermc.engine.paper.entry.entries.EventTrigger
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.utils.server
import fr.xania.dungeons.entries.manifest.DungeonInstance
import fr.xania.dungeons.manager.DungeonManager
import fr.xania.dungeons.manager.WorldManager
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent
import java.io.File
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

        /* An ugly test
        val entry = Query.findById<DungeonInstance>(dungeon.id) ?: return Result.failure(
            IllegalArgumentException("DungeonInstance with id '${dungeon.id}' not found.")
        )

        val firstRoom = entry.children.entry ?: return Result.failure(
            IllegalArgumentException("DungeonInstance with id '${dungeon.id}' has no first room defined.")
        )
        val firstRoomArtifact = firstRoom.artifact.entry!!
        val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)
        val assets = assetManager.fetchBinaryAsset(firstRoomArtifact)!!

        val tempFile = File.createTempFile("plugins/Typewriter/.temp/${firstRoomArtifact.artifactId}", ".nbt")
        tempFile.writeBytes(assets)

        val structure = server.structureManager.loadStructure(tempFile)

        tempFile.delete()
*/

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