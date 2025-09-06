package fr.legendsofxania.dungeons.entries.static

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
import com.typewritermc.engine.paper.entry.entries.ArtifactEntry
import com.typewritermc.engine.paper.utils.loreString
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.name
import fr.legendsofxania.dungeons.managers.TemplateManager
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RoomTemplateContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {
        val entry = context.entryId?.let { entryId ->
            Query.findById<ArtifactEntry>(entryId)
        } ?: return Result.failure(Exception("Entry ID missing or entry not found"))

        bossBar {
            title = "Saving Room Template..."
            color = BossBar.Color.PINK
            progress = 0f
        }

        exit()
        SelectionTool(entry)

        return ok(Unit)
    }

    private class SelectionTool(
        private val entry: ArtifactEntry
    ) : ContentComponent, ItemComponent {
        private var corner1: Location? = null
        private var corner2: Location? = null

        override fun item(player: Player): Pair<Int, IntractableItem> {
            val item = ItemStack(Material.BREEZE_ROD).apply {
                editMeta { meta ->
                    meta.name = "<aqua><b>Selection Tool</b></aqua>"
                    meta.loreString = """
                        <gray><white>Left-click</white> to select the first corner.</gray>
                        <gray><white>Right-click</white> to select the second corner.</gray>
                        <gray><white>Shift + Left-click</white> to save the room.</gray>
                    """.trimIndent()
                }
            } onInteract { event ->
                val location = event.clickedBlock?.location ?: player.location

                when (event.type) {
                    ItemInteractionType.LEFT_CLICK -> {
                        corner1 = location
                        player.msg("First corner selected at <blue>${location.blockX}, ${location.blockY}, ${location.blockZ}</blue>.")
                    }

                    ItemInteractionType.RIGHT_CLICK -> {
                        corner2 = location
                        player.msg("Second corner selected at <blue>${location.blockX}, ${location.blockY}, ${location.blockZ}</blue>.")
                    }

                    ItemInteractionType.SHIFT_LEFT_CLICK -> {
                        val c1 = corner1
                        val c2 = corner2

                        if (c1 == null || c2 == null) {
                            player.msg("<red>Please select both corners before saving the room.")
                            return@onInteract
                        }

                        if (c1.world != c2.world) {
                            player.msg("<red>The two corners must be in the same world.")
                            return@onInteract
                        }

                        Dispatchers.UntickedAsync.launch {
                            TemplateManager.saveRoom(player, c1, c2, entry)
                        }
                    }

                    else -> return@onInteract
                }
            }
            return Pair(4, item)
        }
    }
}