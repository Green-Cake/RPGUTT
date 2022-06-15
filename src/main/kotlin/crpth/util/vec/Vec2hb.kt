package crpth.util.vec

@JvmInline
value class Vec2hb(val data: UByte) {

    companion object {

        val ZERO = Vec2hb(0u, 0u)
        val ONE = Vec2hb(1u, 1u)

    }

    val x: UByte get() = (data.toUInt() shr 4 and 0b1111u).toUByte()
    val y: UByte get() = data and 0b00001111u

    constructor(x: UByte, y: UByte) : this(((x.toUInt() shl 4 and 0b11110000u) + (y and 0b00001111u)).toUByte())

    operator fun plus(other: Vec2hb) = Vec2hb((x + other.x).toUByte(), (y + other.y).toUByte())
    operator fun minus(other: Vec2hb) = Vec2hb((x - other.x).toUByte(), (y - other.y).toUByte())
    operator fun times(other: Vec2hb) = Vec2hb((x * other.x).toUByte(), (y * other.y).toUByte())
    operator fun div(other: Vec2hb) = Vec2hb((x / other.x).toUByte(), (y / other.y).toUByte())
    operator fun rem(other: Vec2hb) = Vec2hb((x % other.x).toUByte(), (y % other.y).toUByte())

    fun getOverflowPlus(other: Vec2hb) = Vec2s(((x + other.x)/16u).toShort(), ((y + other.y)/16u).toShort())

    fun getOverflowMinus(other: Vec2hb) = Vec2s(if(x < other.x) -1 else 0, if(y < other.y) -1 else 0)

    override fun toString() = "$x, $y"

}