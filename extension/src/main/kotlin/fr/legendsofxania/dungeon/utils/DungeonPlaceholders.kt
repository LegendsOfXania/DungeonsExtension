package fr.legendsofxania.dungeon.utils

import com.typewritermc.core.extension.annotations.Singleton
import com.typewritermc.engine.paper.extensions.placeholderapi.PlaceholderHandler
import fr.legendsofxania.dungeon.managers.PlayerManager
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Singleton
class DungeonPlaceholders : PlaceholderHandler, KoinComponent {
    private val playerManager: PlayerManager by inject()

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (player == null) return null
        if (params != "in_dungeon") return null

        return if (playerManager.isInDungeon(player)) "1" else "0"
    }
}