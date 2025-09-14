package fr.legendsofxania.dungeons.entries.manifest.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.AudienceFilter
import com.typewritermc.engine.paper.entry.entries.AudienceFilterEntry
import com.typewritermc.engine.paper.entry.entries.Invertible
import fr.legendsofxania.dungeons.entries.manifest.DungeonDefinition
import fr.legendsofxania.dungeons.managers.PlayerManager
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

@Entry("in_dungeon_audience", "Return if a player is in a dungeon or not.", Colors.GREEN, "tabler:building")
/**
 * The `In Dungeon Audience` entry is used to check if a player is currently in a dungeon.
 *
 * ## How could this be used?
 *
 * This could be used to apply effects to players who are in a dungeon.
 */
class InDungeonAudience(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<out AudienceEntry>> = emptyList(),
    val dungeon: Optional<Ref<DungeonDefinition>> = Optional.empty(),
    override val inverted: Boolean = false,
) : AudienceFilterEntry, Invertible {
    override suspend fun display(): AudienceFilter = InDungeonAudienceFilter(dungeon, inverted, ref())
}

class InDungeonAudienceFilter(
    private val dungeon: Optional<Ref<DungeonDefinition>>,
    private val inverted: Boolean,
    ref: Ref<out AudienceFilterEntry>
) : AudienceFilter(ref), KoinComponent {
    private val playerManager: PlayerManager by inject()

    override fun filter(player: Player): Boolean {
        val playerDungeon = playerManager.getDungeon(player)

        val isInTargetDungeon = if (dungeon.isPresent) {
            val targetDungeon = dungeon.get()
            playerDungeon?.definition == targetDungeon
        } else {
            playerDungeon != null
        }

        return if (inverted) {
            !isInTargetDungeon
        } else {
            isInTargetDungeon
        }
    }
}