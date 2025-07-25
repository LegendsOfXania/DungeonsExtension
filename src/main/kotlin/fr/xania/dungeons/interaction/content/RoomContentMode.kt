package fr.xania.dungeons.interaction.content

import com.typewritermc.core.entries.Query
import com.typewritermc.core.utils.UntickedAsync
import com.typewritermc.core.utils.launch
import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.IntractableItem
import com.typewritermc.engine.paper.content.components.ItemComponent
import com.typewritermc.engine.paper.content.components.ItemInteractionType
import com.typewritermc.engine.paper.content.components.bossBar
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.components.onInteract
import com.typewritermc.engine.paper.content.entryId
import com.typewritermc.engine.paper.utils.loreString
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.name
import com.typewritermc.engine.paper.utils.server
import com.typewritermc.engine.paper.entry.AssetManager
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.java.KoinJavaComponent
import java.io.File

class RoomContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {
        val entry = Query.findById<ArtifactEntry>(context.entryId!!)
            ?: return Result.failure(IllegalStateException("ArtifactEntry not found."))
        bossBar {
            title = "Saving a RoomArtifact"
            color = BossBar.Color.BLUE
            progress = 1f
        }
        exit()

        +ContentItem(entry)

        return ok(Unit)
    }

    private class ContentItem(
        private val entry: ArtifactEntry
    ) : ContentComponent, ItemComponent {
        private var corner1: Location? = null
        private var corner2: Location? = null

        override fun item(player: Player): Pair<Int, IntractableItem> {
            val selectionItem = ItemStack(Material.BREEZE_ROD).apply {
                editMeta { meta ->
                    meta.name = "<aqua><b>Selection</b>"
                    meta.loreString = """
            <gray><white>Left-click</white> to select the second corner.</gray>
            <gray><white>Right-click</white> to select the first corner.</gray>
            <gray><white>Shift + Left-click</white> to save the room.</gray>
        """.trimIndent()
                }
            } onInteract { event ->
            val location = event.clickedBlock?.location ?: player.location

            when (event.type) {
                ItemInteractionType.LEFT_CLICK -> {
                    corner1 = location
                    player.msg("First corner set to <blue>${location.blockCoords()}</blue>.")
                }

                ItemInteractionType.RIGHT_CLICK -> {
                    corner2 = location
                    player.msg("Second corner set to <blue>${location.blockCoords()}</blue>.")
                }

                ItemInteractionType.SHIFT_LEFT_CLICK -> {
                    saveRoom(player, corner1, corner2)
                }

                else -> return@onInteract
                }
            }

            return Pair(4, selectionItem)
        }

        private fun Location.blockCoords() = "$blockX, $blockY, $blockZ"

        private fun saveRoom(player: Player, corner1: Location?, corner2: Location?) {
            if (corner1 == null || corner2 == null) {
                player.msg("You must select both corners before saving the room.")
                return
            }

            if (corner1.world != corner2.world) {
                player.msg("Both corners must be in the same world.")
                return
            }

            Dispatchers.UntickedAsync.launch {
                val structure = server.structureManager.createStructure()
                structure.fill(corner1, corner2, true)

                val tempFile = File.createTempFile("plugins/Typewriter/temp/room", ".nbt")
                server.structureManager.saveStructure(tempFile, structure)

                val assetManager = KoinJavaComponent.get<AssetManager>(AssetManager::class.java)
                assetManager.storeBinaryAsset(entry, tempFile.readBytes())

                tempFile.delete()
            }

            player.msg("Room saved successfully!")
        }

        override suspend fun initialize(player: Player) {}
        override suspend fun tick(player: Player) {}
        override suspend fun dispose(player: Player) {}
    }
}