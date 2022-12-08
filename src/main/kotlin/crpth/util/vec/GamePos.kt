package crpth.util.vec

import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * @param data x: uint y: uint
 */
@JvmInline
value class GamePos(override val value: Vec2i) : IVec2nWrapper<Int> {

    companion object {

        const val BITS_FOR_SUBPIXEL = 8
        private const val PIXEL_AREA = (0b1 shl BITS_FOR_SUBPIXEL)

        val ZERO = GamePos(Vec2i.ZERO)

        fun readFrom(stream: DataInputStream) = GamePos(Vec2i.readFrom(stream))

        fun tile(x: Int, y: Int) = GamePos(x, y)

        fun sub(sx: Int, sy: Int) = GamePos(sx / PIXEL_AREA, sy / PIXEL_AREA,
            (sx % PIXEL_AREA).toByte(), (sy % PIXEL_AREA).toByte()
        )

    }

    val posInTiles get() = Vec2i(x / PIXEL_AREA, y / PIXEL_AREA)

    val subpos get() = Vec2i(x % PIXEL_AREA, y % PIXEL_AREA)

    private constructor(x: Int, y: Int, sx: Byte=0, sy: Byte=0) : this(
        Vec2i((x shl BITS_FOR_SUBPIXEL) + sx, (y shl BITS_FOR_SUBPIXEL) + sy)
    )

    operator fun plus(other: GamePos) = GamePos(value + other.value)

    operator fun minus(other: GamePos) = GamePos(value - other.value)

    fun plus(x: Int, y: Int) = plus(GamePos(Vec2i(x, y)))

    fun minus(x: Int, y: Int) = minus(GamePos(Vec2i(x, y)))

    override fun toString() = "($posInTiles, $subpos)"

    override fun toVec2f() = Vec2f(x.toFloat() / PIXEL_AREA, y.toFloat() / PIXEL_AREA)

    override fun toVec2d() = Vec2d(x.toDouble() / PIXEL_AREA, y.toDouble() / PIXEL_AREA)

    fun encode(stream: DataOutputStream) {
        stream.writeLong(value.data.toLong())
    }

}