package fr.xania.dungeons.content

import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.IntractableItem
import com.typewritermc.engine.paper.content.components.ItemInteractionType
import com.typewritermc.engine.paper.content.components.ItemsComponent
import com.typewritermc.engine.paper.content.components.bossBar
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.components.onInteract
import com.typewritermc.engine.paper.content.entryId
import com.typewritermc.engine.paper.utils.loreString
import com.typewritermc.engine.paper.utils.msg
import com.typewritermc.engine.paper.utils.name
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RoomContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {
        val id = context.entryId ?: "unknown"

        bossBar {
            title = "Saving SimpleRoomDefinition"
            color = BossBar.Color.BLUE
            progress = 1f
        }
        exit()

        +SimpleItems(id)

        return ok(Unit)
    }

    private class SimpleItems(
        private val artifactId: String
    ) : ContentComponent, ItemsComponent {
        override fun items(player: Player): Map<Int, IntractableItem> {
            var playerCorners = Pair<Location?, Location?>(null, null)

            val selectionItem = ItemStack(Material.BREEZE_ROD).apply {
                editMeta { meta ->
                    meta.name = "<aqua><b>Selection</b>"
                    meta.loreString = """
            <gray><white>Left-click</white> to select the second corner.</gray>
            <gray><white>Right-click</white> to select the first corner.</gray>
            <gray><white>Shift + Right-click</white> to save the room.</gray>
        """.trimIndent()
                }
            } onInteract {
                val location = it.clickedBlock?.location ?: player.location

                when (it.type) {
                    ItemInteractionType.LEFT_CLICK -> {
                        val firstCorner = Saving().selectCorner(player, location, true)
                        playerCorners = playerCorners.copy(first = firstCorner)
                    }
                    ItemInteractionType.RIGHT_CLICK -> {
                        val secondCorner = Saving().selectCorner(player, location, false)
                        playerCorners = playerCorners.copy(second = secondCorner)
                    }
                    ItemInteractionType.SHIFT_RIGHT_CLICK -> {
                        val (firstCorner, secondCorner) = playerCorners
                        if (firstCorner == null || secondCorner == null) {
                            player.msg("You must select both corners before saving the room.")
                        }
                        if (firstCorner!!.world != secondCorner!!.world) {
                            player.msg("The corners must be in the same world.")
                        }
                        Saving().savingRoom(player, firstCorner, secondCorner, artifactId)
                    }
                    else -> return@onInteract
                }
            }
            val doorsItem = ItemStack(Material.STRUCTURE_VOID).apply {
                editMeta { meta ->
                    meta.name = "<aqua><b>Doors</b>"
                    meta.loreString = """
                        <gray><white>Left-click</white> to place an incoming door.</gray>
                        <gray><white>Right-click</white> to place an outgoing door.</gray>
                        <gray><white>Shift + Left-click</white> to remove the door.</gray>
                    """.trimIndent()
                }
            } onInteract {
                return@onInteract
            }

            return mapOf(
                3 to selectionItem,
                5 to doorsItem
            )
        }

        override suspend fun initialize(player: Player) {}
        override suspend fun tick(player: Player) {}
        override suspend fun dispose(player: Player) {}
    }
}