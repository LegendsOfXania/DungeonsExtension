package fr.legendsofxania.dungeons.entries.static.variable

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.exceptions.ContextDataNotFoundException
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.VariableData
import com.typewritermc.core.extension.annotations.WithRotation
import com.typewritermc.core.utils.point.Coordinate
import com.typewritermc.core.utils.point.toPosition
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.utils.toPosition
import com.typewritermc.engine.paper.utils.toWorld
import fr.legendsofxania.dungeons.entries.manifest.dungeon.RoomDefinition
import fr.legendsofxania.dungeons.managers.InstancesManager
import fr.legendsofxania.dungeons.managers.WorldManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Entry(
    "relative_room_position_variable",
    "A variable representing a position relative to a room",
    Colors.GREEN,
    "tabler:arrows-diagonal"
)
@VariableData(RelativeRoomPositionVariableData::class)
class RelativeRoomPositionVariable(
    override val id: String = "",
    override val name: String = "",
) : VariableEntry, KoinComponent {
    private val instancesManager: InstancesManager by inject()

    override fun <T : Any> get(context: VarContext<T>): T {
        val data = context.getData<RelativeRoomPositionVariableData>()
            ?: throw ContextDataNotFoundException(context.klass, context.data)

        val bukkitWorld = WorldManager().getWorld()
            ?: throw IllegalStateException("Could not find the dungeon world.")

        val coordinate = data.coordinate.get(context.player, context.interactionContext)

        val position = coordinate.toPosition(bukkitWorld.toWorld())
        val roomInstance = instancesManager.getInstance(data.room)
            ?: throw IllegalStateException("Could not find an active instance for room ${data.room.id}.")

        val origin = roomInstance.box.min.toLocation(bukkitWorld).toPosition()

        @Suppress("UNCHECKED_CAST")
        return origin.add(position) as T
    }
}

data class RelativeRoomPositionVariableData(
    @WithRotation
    val coordinate: Var<Coordinate> = ConstVar(Coordinate.ORIGIN),
    val room: Ref<RoomDefinition>
)