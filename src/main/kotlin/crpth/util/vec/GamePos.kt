package crpth.util.vec

import java.io.DataInputStream

/**
 * @param data x: uint y: uint z: uint (4bit + 28bit)
 */
@JvmInline
value class GamePos(val data: Vec3i) {

    companion object {

        val ZERO = GamePos(Vec3i.ZERO)

        fun readFrom(stream: DataInputStream) = GamePos(Vec3i(stream.readInt(), stream.readInt(), stream.readInt()))

    }

    val pixel get() = Vec2i(data.x / 0b10000, data.y / 0b10000)

    val subpixel get() = Vec2i(data.x % 0b10000, data.y % 0b10000)

    val z get() = data.z

    constructor(x: Int, y: Int, sx: Byte=0, sy: Byte=0, z: Int=0) : this(
        Vec3i((x shl  4) + sx, (y shl 4) + sy, z)
    )

    operator fun plus(other: GamePos) = GamePos(data + other.data)

    operator fun minus(other: GamePos) = GamePos(data - other.data)

    fun plus(x: Int, y: Int) = GamePos(Vec3i(data.x + x, data.y + y, data.z))

    fun minus(x: Int, y: Int) = GamePos(Vec3i(data.x - x, data.y - y, data.z))

    override fun toString() = "($pixel, $subpixel, $z)"

    fun toVec2f() = Vec2f(data.x.toFloat() / 0b10000, data.y.toFloat() / 0b10000)

    fun toVec2d() = Vec2d(data.x.toDouble() / 0b10000, data.y.toDouble() / 0b10000)

}