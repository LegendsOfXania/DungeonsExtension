package fr.legendsofxania.dungeons.events

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.RoomDefinition
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event triggered when a player joins a room.
 *
 * @param player The player who joined the room.
 * @param room The reference to the room definition.
 */
class AsyncOnPlayerJoinRoomEvent(
    val player: Player,
    val room: Ref<RoomDefinition>
) : Event(true) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}