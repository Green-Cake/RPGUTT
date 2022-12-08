package crpth.util.vec

import org.lwjgl.opengl.GL11

@JvmInline
value class Vec2f(val data: ULong) : IVec2n<Float> {

    companion object {

        val ZERO = Vec2f(0.0f, 0.0f)
        val ONE = Vec2f(1.0f, 1.0f)

    }

    constructor(x: Float, y: Float) : this((x.toRawBits().resizeToULong() shl 32) + (y).toRawBits().resizeToULong())

    override val x get() = Float.fromBits((data shr 32).toInt())

    override val y get() = Float.fromBits(data.toInt())

    operator fun plus(other: Vec2f) = Vec2f(x + other.x, y + other.y)

    operator fun minus(other: Vec2f) = Vec2f(x - other.x, y - other.y)

    operator fun times(other: Vec2f) = Vec2f(x * other.x, y * other.y)

    operator fun div(other: Vec2f) = Vec2f(x / other.x, y / other.y)

    fun plus(x: Float, y: Float) = Vec2f(this.x + x, this.y + y)

    fun minus(x: Float, y: Float) = Vec2f(this.x - x, this.y - y)

    fun times(x: Float, y: Float) = Vec2f(this.x * x, this.y * y)

    fun div(x: Float, y: Float) = Vec2f(this.x / x, this.y / y)

    operator fun times(other: Float) = Vec2f(x * other, y * other)

    operator fun div(other: Float) = Vec2f(x / other, y / other)

    override fun toString() = "($x, $y)"

    operator fun component1() = x

    operator fun component2() = y

}