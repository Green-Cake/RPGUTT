package crpth.util.vec

import org.lwjgl.opengl.GL11

@JvmInline
value class Vec2s(val data: UInt) : IVec2n<Short> {

    companion object {

        val ZERO = Vec2i(0, 0)
        val ONE = Vec2i(1, 1)

    }

    override val x: Short
        get() = (data shr 16).toShort()
    override val y: Short
        get() = data.toShort()

    constructor(x: Short, y: Short) : this((x.resizeToUInt() shl 16) + y.resizeToUInt())

    constructor(x: Int, y: Int) : this(x.toShort(), y.toShort())

    override fun coerceIn(xRange: ClosedRange<Short>, yRange: ClosedRange<Short>) = Vec2s(x.coerceIn(xRange), y.coerceIn(yRange))

    operator fun plus(other: Vec2s) = Vec2s(x + other.x, y + other.y)

    operator fun minus(other: Vec2s) = Vec2s(x - other.x, y - other.y)

    operator fun times(other: Vec2s) = Vec2s(x * other.x, y * other.y)

    operator fun div(other: Vec2s) = Vec2s(x / other.x, y / other.y)

    fun plus(x: Short, y: Short) = Vec2s(this.x + x, this.y + y)

    fun minus(x: Short, y: Short) = Vec2s(this.x - x, this.y - y)

    fun times(x: Short, y: Short) = Vec2s(this.x * x, this.y * y)

    fun div(x: Short, y: Short) = Vec2s(this.x / x, this.y / y)

    operator fun times(other: Int) = Vec2s(x * other, y * other)

    operator fun div(other: Int) = Vec2s(x / other, y / other)

    override fun toString() = "($x, $y)"

    operator fun unaryMinus() = times(-1)

}