package crpth.util.vec

import java.io.DataInputStream

/**
 * @param data x(16) y(16) UNUSED(8) sx(4) sy(4) z(16)
 */
@JvmInline
value class GamePos(val data: Vec4s) {

    companion object {

        const val SUBPIXEL_MAX = 16u

        fun readFrom(stream: DataInputStream) = GamePos(Vec4s(stream.readLong().toULong()))

    }

    val pixel get() = Vec2s(data.x, data.y)

    val subpixel get() = Vec2hb(data.z.toUByte())

    val z get() = data.w.toUShort()

    constructor(x: UShort, y: UShort, sx: UByte=0u, sy: UByte=0u, z: UShort=0u) : this(
        Vec4s(
        (x + sx/16u).toShort(),
        (y + sy/16u).toShort(),
        Vec2hb(sx, sy).data.toShort(),
        z.toShort()
    )
    )

    constructor(pos: Vec2s, subpixel: Vec2hb = Vec2hb.ZERO, z: UShort=0u) : this(pos.x.toUShort(), pos.y.toUShort(), subpixel.x, subpixel.y, z)

    operator fun plus(other: GamePos) = GamePos(
        pixel + other.pixel + subpixel.getOverflowPlus(other.subpixel),
        subpixel + other.subpixel,
        (z + other.z).toUShort())

    operator fun minus(other: GamePos) = GamePos(pixel - other.pixel + subpixel.getOverflowMinus(other.subpixel), subpixel - other.subpixel, (z - other.z).toUShort())

    fun plusSubUnsigned(sx: UByte, sy: UByte) = this + GamePos(0u, 0u, sx, sy)

    fun minusSubUnsigned(sx: UByte, sy: UByte) = this - GamePos(0u, 0u, sx, sy)

    fun plusSub(sx: Int=0, sy: Int=0): GamePos {

        var result = this

        if(sx != 0) result = if(sx > 0) result.plusSubUnsigned(sx.toUByte(), 0u) else result.minusSubUnsigned((-sx).toUByte(), 0u)

        if(sy != 0) result = if(sy > 0) result.plusSubUnsigned(0u, sy.toUByte()) else result.minusSubUnsigned(0u, (-sy).toUByte())

        return result

    }

    fun minusSub(sx: Int=0, sy: Int=0): GamePos = plusSub(-sx, -sy)

    override fun toString() = "($pixel, $subpixel, $z)"

    fun toVec2f() = Vec2f(pixel.x.toFloat() + subpixel.x.toFloat()/16f, pixel.y.toFloat() + subpixel.y.toFloat()/16f)

}