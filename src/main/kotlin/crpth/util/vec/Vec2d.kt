package crpth.util.vec

import org.lwjgl.opengl.GL11


data class Vec2d(override val x: Double, override val y: Double) : IVec2n<Double> {

    companion object {

        val ZERO = Vec2d(0.0, 0.0)
        val ONE = Vec2d(1.0, 1.0)

    }

    override fun setVertex() {
        GL11.glVertex2d(x, y)
    }

    operator fun plus(other: Vec2d) = Vec2d(x + other.x, y + other.y)

    operator fun minus(other: Vec2d) = Vec2d(x - other.x, y - other.y)

    operator fun times(other: Vec2d) = Vec2d(x * other.x, y * other.y)

    operator fun div(other: Vec2d) = Vec2d(x / other.x, y / other.y)

    fun plus(x: Double, y: Double) = Vec2d(this.x + x, this.y + y)

    fun minus(x: Double, y: Double) = Vec2d(this.x - x, this.y - y)

    fun times(x: Double, y: Double) = Vec2d(this.x * x, this.y * y)

    fun div(x: Double, y: Double) = Vec2d(this.x / x, this.y / y)

    operator fun times(other: Double) = Vec2d(x * other, y * other)

    operator fun div(other: Double) = Vec2d(x / other, y / other)

    operator fun unaryMinus() = times(-1.0)


}