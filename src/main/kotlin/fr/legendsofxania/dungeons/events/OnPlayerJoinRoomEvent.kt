package fr.legendsofxania.dungeons.events

import com.typewritermc.core.entries.Ref
import fr.legendsofxania.dungeons.entries.manifest.RoomInstance
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class OnPlayerJoinRoomEvent(
    val player: Player,
    val dungeon: Ref<RoomInstance>
) : Event() {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}