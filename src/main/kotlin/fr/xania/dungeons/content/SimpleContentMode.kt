package fr.xania.dungeons.content

import com.typewritermc.core.utils.ok
import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.IntractableItem
import com.typewritermc.engine.paper.content.components.ItemsComponent
import com.typewritermc.engine.paper.content.components.bossBar
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.components.onInteract
import com.typewritermc.engine.paper.utils.loreString
import com.typewritermc.engine.paper.utils.name
import net.kyori.adventure.bossbar.BossBar
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SimpleContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    override suspend fun setup(): Result<Unit> {

        bossBar {
            title = "Saving SimpleRoomDefinition"
            color = BossBar.Color.BLUE
            progress = 1f
        }
        exit()

        +SimpleItems()

        return ok(Unit)
    }

    private class SimpleItems : ContentComponent, ItemsComponent {
        override fun items(player: Player): Map<Int, IntractableItem> {

            val selectionItem = ItemStack(Material.BREEZE_ROD).apply {
                editMeta { meta ->
                    meta.name = "<aqua>Selection"
                    meta.loreString = """
                        <gray><white>Right-click</white> to select the first corner.</gray>
                        <gray><white>Left-click</white> to select the second corner.</gray>
                        <gray><white>Shift + Left-click</white> to save the room.</gray>
                    """.trimIndent()
                }
            } onInteract {
                TODO("Implement selection and saving logic")
            }

            return mapOf(
                2 to selectionItem
            )
        }

        override suspend fun initialize(player: Player) {}
        override suspend fun tick(player: Player) {}
        override suspend fun dispose(player: Player) {}
    }
}