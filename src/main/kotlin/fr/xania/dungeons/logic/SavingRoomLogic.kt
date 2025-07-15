package fr.xania.dungeons.logic

import com.typewritermc.engine.paper.utils.msg
import org.bukkit.Location
import org.bukkit.entity.Player

object SavingRoomLogic {

    private var firstCorner: Location? = null
    private var secondCorner: Location? = null

    fun selectCorner(location: Location, isFirst: Boolean, player: Player) {
        if (isFirst) {
            firstCorner = location
            player.msg("You have selected the first corner at <blue>${location.x}</blue>, <blue>${location.y}</blue>, <blue>${location.z}</blue>.")
        } else {
            secondCorner = location
            player.msg("You have selected the second corner at <blue>${location.x}</blue>, <blue>${location.y}</blue>, <blue>${location.z}</blue>.")
        }
    }

    fun saveRoom(player: Player) {
        TODO("Implement saving logic here")
    }
}