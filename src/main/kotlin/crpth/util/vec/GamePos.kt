package crpth.util.vec

import java.io.DataInputStream

/**
 * @param data x: uint y: uint z: uint (4bit + 28bit)
 */
@JvmInline
value class GamePos(val data: Vec3i) {

    companion object {

        const val BITS_FOR_SUBPIXEL = 8
        private const val PIXEL_AREA = (0b1 shl BITS_FOR_SUBPIXEL)

        val ZERO = GamePos(Vec3i.ZERO)

        fun readFrom(stream: DataInputStream) = GamePos(Vec3i(stream.readInt(), stream.readInt(), stream.readInt()))

        fun sub(sx: Int, sy: Int) = GamePos(sx / PIXEL_AREA, sy / PIXEL_AREA,
            (sx % PIXEL_AREA).toByte(), (sy % PIXEL_AREA).toByte()
        )

    }

    val pixel get() = Vec2i(data.x / PIXEL_AREA, data.y / PIXEL_AREA)

    val subpixel get() = Vec2i(data.x % PIXEL_AREA, data.y % PIXEL_AREA)

    val z get() = data.z

    constructor(x: Int, y: Int, sx: Byte=0, sy: Byte=0, z: Int=0) : this(
        Vec3i((x shl BITS_FOR_SUBPIXEL) + sx, (y shl BITS_FOR_SUBPIXEL) + sy, z)
    )

    operator fun plus(other: GamePos) = GamePos(data + other.data)

    operator fun minus(other: GamePos) = GamePos(data - other.data)

    fun plus(x: Int, y: Int) = GamePos(Vec3i(data.x + x, data.y + y, data.z))

    fun minus(x: Int, y: Int) = GamePos(Vec3i(data.x - x, data.y - y, data.z))

    override fun toString() = "($pixel, $subpixel, $z)"

    fun toVec2f() = Vec2f(data.x.toFloat() / PIXEL_AREA, data.y.toFloat() / PIXEL_AREA)

    fun toVec2d() = Vec2d(data.x.toDouble() / PIXEL_AREA, data.y.toDouble() / PIXEL_AREA)

}