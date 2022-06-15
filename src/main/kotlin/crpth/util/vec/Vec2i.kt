package crpth.util.vec

import org.lwjgl.opengl.GL11

@JvmInline
value class Vec2i(val data: ULong) : IVec2n<Int> {

    companion object {

        val ZERO = Vec2i(0, 0)
        val ONE = Vec2i(1, 1)

    }

    override val x: Int
        get() = (data shr 32).toInt()

    override val y: Int
        get() = (data).toInt()

    constructor(x: Int, y: Int) : this((x.resizeToULong() shl 32) + y.resizeToULong())

    override fun setVertex() {
        GL11.glVertex2i(x, y)
    }

    operator fun plus(other: Vec2i) = Vec2i(x + other.x, y + other.y)

    operator fun minus(other: Vec2i) = Vec2i(x - other.x, y - other.y)

    operator fun times(other: Vec2i) = Vec2i(x * other.x, y * other.y)

    operator fun div(other: Vec2i) = Vec2i(x / other.x, y / other.y)

    fun plus(x: Int, y: Int) = Vec2i(this.x + x, this.y + y)

    fun minus(x: Int, y: Int) = Vec2i(this.x - x, this.y - y)

    fun times(x: Int, y: Int) = Vec2i(this.x * x, this.y * y)

    fun div(x: Int, y: Int) = Vec2i(this.x / x, this.y / y)

    operator fun times(other: Int) = Vec2i(x * other, y * other)

    operator fun div(other: Int) = Vec2i(x / other, y / other)

    override fun toString() = "($x, $y)"

    operator fun component1() = x

    operator fun component2() = y

    operator fun unaryMinus() = times(-1)

}