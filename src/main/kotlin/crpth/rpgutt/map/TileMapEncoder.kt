package crpth.rpgutt.map

import crpth.util.vec.Vec2b
import java.nio.ByteBuffer

@ExperimentalUnsignedTypes
object TileMapEncoder {

    val PREFIX = Vec2b(0xAB, 0xEF)
    val POSTFIX = Vec2b(0xEF, 0xAB)

    /**
     *
     * AB EF width_2 height_2 tiles_(2 * width * height) num_2 { id_2 req_2 buf_(req) }_num EF AB
     *
     * @param stream a stream to get the map binary data.
     */
    fun encode(map: TileMap): ByteArray {

        val buffer = ByteBuffer.allocate(11 + map.size.x*map.size.y*2*map.layerCount + map.entityFactories.sumOf { it.meta.size + 4 })
        buffer.putShort(PREFIX.data.toShort())

        buffer.putInt(map.size.data.toInt())

        buffer.put(map.layerCount.toByte())

        for(l in 0 until map.layerCount) for(i in 0 until map.size.x*map.size.y) {
            buffer.putShort(map.tiles[l][i].toShort())
        }

        buffer.putShort(map.entityFactories.size.toShort())

        map.entityFactories.forEach {
            buffer.putShort(it.id.toShort())
            buffer.putShort(it.meta.size.toShort())
            buffer.put(it.meta)
        }

        buffer.putShort(POSTFIX.data.toShort())

        return buffer.array().copyOfRange(0, buffer.position())

    }

}