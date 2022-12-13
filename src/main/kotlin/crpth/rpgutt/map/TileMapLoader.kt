package crpth.rpgutt.map

import crpth.rpgutt.RpgUtt
import crpth.util.vec.Vec2b
import crpth.util.vec.Vec2s
import crpth.util.vec.readFrom
import crpth.util.vec.readString
import java.io.DataInputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

object TileMapLoader {

    val PREFIX = Vec2b(0xAB, 0xEF)
    val POSTFIX = Vec2b(0xEF, 0xAB)

    /**
     *
     * AB EF width_2 height_2 tiles_(2 * width * height) num_2 { id_2 req_2 buf_(req) }_num EF AB
     *
     * @param stream a stream to get the map binary data.
     */
    fun load(name: String, _stream: InputStream): TileMap? {

        val zis = ZipInputStream(_stream)

        zis.getNextEntry()

        val stream = DataInputStream(zis)

        val p = Vec2b.readFrom(stream)

        if(p != PREFIX) {

            RpgUtt.logger.warn("An illegal file format(prefix: ${p.data.toString(16)}) for TileMap!")

            return null
        }

        val size = Vec2s.readFrom(stream)
        val amount = size.x * size.y

        val tileSetsCount = stream.readByte()

        val tileSets = List(tileSetsCount.toInt()) {
            stream.readString()
        }

        val layerCount = stream.readByte()

        val tiles = Array(layerCount.toInt()) {
            UShortArray(amount) {
                stream.readShort().toUShort()
            }
        }

        val numFactory = stream.readShort()

        val factories = mutableListOf<EntityFactory>()

        repeat(numFactory.toInt()) {

            val id = stream.readShort().toUShort()

            val req = stream.readShort()

            factories += EntityFactory(id, ByteArray(req.toUShort().toInt()) { stream.read().toByte() })

        }

        val post = Vec2b.readFrom(stream)

        if(post != POSTFIX) {

            RpgUtt.logger.warn("An illegal file format(postfix: ${post.data.toString(16)}) for TileMap!")

            return null

        }

        return TileMap(name, size, tileSets, tiles, factories)

    }

    fun load(name: String) = load(name, ClassLoader.getSystemResourceAsStream("assets/rpgutt/levels/$name.level")!!)

}