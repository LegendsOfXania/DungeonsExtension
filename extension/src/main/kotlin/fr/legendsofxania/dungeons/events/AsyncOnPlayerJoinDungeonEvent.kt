package fr.legendsofxania.dungeons.events

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.dungeon.DungeonDefinition
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when a player joins a dungeon.
 *
 * @param player The player who joined the dungeon.
 * @param dungeon The reference to the dungeon definition.
 */
class AsyncOnPlayerJoinDungeonEvent(
    val player: Player,
    val dungeon: Ref<DungeonDefinition>
) : Event(true) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}