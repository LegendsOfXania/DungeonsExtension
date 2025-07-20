package fr.xania.dungeons.content


import com.typewritermc.engine.paper.utils.msg
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.TileState
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.entity.Player
import java.io.File

@Serializable
data class Block(
    val x: Int,
    val y: Int,
    val z: Int,
    val blockType: String,
    val blockState: String? = null
)

@Serializable
data class Room(
    val blocks: List<Block>
)

class Saving {
    private val json = Json { prettyPrint = true }

    private fun serializeBlockState(state: TileState): String {
        val world = state.world as CraftWorld
        val blockPos = BlockPos(state.x, state.y, state.z)
        val tileEntity: BlockEntity? = world.handle.getBlockEntity(blockPos)
        return if (tileEntity != null) {
            val nbt = tileEntity.saveWithFullMetadata(world.handle.registryAccess())
            nbt.toString()
        } else ""
    }

    fun savingRoom(player: Player, p0: Location, p1: Location, artifactId: String) {
        val world = p0.world ?: return

        val minX = minOf(p0.blockX, p1.blockX)
        val minY = minOf(p0.blockY, p1.blockY)
        val minZ = minOf(p0.blockZ, p1.blockZ)
        val maxX = maxOf(p0.blockX, p1.blockX)
        val maxY = maxOf(p0.blockY, p1.blockY)
        val maxZ = maxOf(p0.blockZ, p1.blockZ)

        val blocks = mutableListOf<Block>()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)
                    if (block.type != Material.AIR) {
                        val relX = x - minX
                        val relY = y - minY
                        val relZ = z - minZ
                        val blockStateData = (block.state as? TileState)?.let { serializeBlockState(it) }

                        blocks.add(
                            Block(
                                x = relX,
                                y = relY,
                                z = relZ,
                                blockType = block.type.name,
                                blockState = blockStateData
                            )
                        )
                    }
                }
            }
        }

        val room = Room(blocks = blocks)

        val outputFolder = File("plugins/Typewriter/assets/artifacts")
        outputFolder.mkdirs()

        val outputFile = File(outputFolder, "$artifactId.json")
        outputFile.writeText(json.encodeToString(room))

        player.msg("Room <blue>$artifactId</blue> saved successfully.")
    }


    fun selectCorner(player: Player, location: Location, isFirst: Boolean): Location {
        if (isFirst) {
            player.msg("First corner selected at <blue>${location.blockX}</blue>, <blue>${location.blockY}</blue>, <blue>${location.blockZ}</blue>.")
        } else {
            player.msg("Second corner selected at <blue>${location.blockX}</blue>, <blue>${location.blockY}</blue>, <blue>${location.blockZ}</blue>.")
        }

        return location
    }
}