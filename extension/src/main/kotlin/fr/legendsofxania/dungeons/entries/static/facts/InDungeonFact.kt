package fr.legendsofxania.dungeons.entries.static.facts

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import fr.legendsofxania.dungeons.entries.manifest.dungeon.DungeonDefinition
import fr.legendsofxania.dungeons.managers.PlayerManager
import fr.legendsofxania.dungeons.utils.toInt
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

@Entry("in_dungeon_fact", "If the player is in a dungeon", Colors.PURPLE, "eos-icons:storage-class")
/**
 * The 'In Dungeon Fact' is a fact that returns 1 if the player has an active dungeon, and 0 if not.
 *
 * If no dungeon is referenced, it will filter based on if any dungeon is active.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * With this fact, it is possible to make an entry only take action if the player does not have an active dungeon.
 */
class InDungeonFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("When not set, it will filter based on if any dungeon is active.")
    val dungeon: Optional<Ref<DungeonDefinition>> = Optional.empty(),
) : ReadableFactEntry, KoinComponent {
    private val playerManager: PlayerManager by inject()

    override fun readSinglePlayer(player: Player): FactData {
        val playerDungeon = playerManager.getDungeon(player)

        val isInTargetDungeon = if (dungeon.isPresent) {
            val targetDungeon = dungeon.get()
            playerDungeon?.definition == targetDungeon
        } else {
            playerDungeon != null
        }

        return FactData(isInTargetDungeon.toInt())
    }
}
