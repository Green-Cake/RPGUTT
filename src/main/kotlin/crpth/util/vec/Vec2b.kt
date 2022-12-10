package crpth.util.vec

import org.lwjgl.opengl.GL11

@JvmInline
value class Vec2b(val data: UShort) : IVec2n<Byte> {

    companion object {

        val ZERO = Vec2b(0, 0)
        val ONE = Vec2b(1, 1)

    }

    override val x: Byte
        get() = (data.toShort().resizeToUInt() shr 8).toByte()
    override val y: Byte
        get() = data.toByte()

    override fun coerceIn(xRange: ClosedRange<Byte>, yRange: ClosedRange<Byte>) = Vec2b(x.coerceIn(xRange), y.coerceIn(yRange))

    constructor(x: Byte, y: Byte) : this(
        (((x.toUInt() and 0xFFu) shl 8) + (y.toUInt() and 0xFFu)).toUShort()
    )

    constructor(x: Int, y: Int) : this(x.toByte(), y.toByte())

    operator fun plus(other: Vec2b) = Vec2b(x + other.x, y + other.y)

    operator fun minus(other: Vec2b) = Vec2b(x - other.x, y - other.y)

    operator fun times(other: Vec2b) = Vec2b(x * other.x, y * other.y)

    operator fun div(other: Vec2b) = Vec2b(x / other.x, y / other.y)

    fun plus(x: Short, y: Short) = Vec2b(this.x + x, this.y + y)

    fun minus(x: Short, y: Short) = Vec2b(this.x - x, this.y - y)

    fun times(x: Short, y: Short) = Vec2b(this.x * x, this.y * y)

    fun div(x: Short, y: Short) = Vec2b(this.x / x, this.y / y)

    operator fun times(other: Int) = Vec2b(x * other, y * other)

    operator fun div(other: Int) = Vec2b(x / other, y / other)

    operator fun unaryMinus() = times(-1)

    override fun toString() = "($x, $y)"

    operator fun component1() = x

    operator fun component2() = y

}