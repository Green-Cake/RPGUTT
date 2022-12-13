package crpth.util.vec


data class Vec4f(val a: Float, val b: Float, val c: Float, val d: Float=1.0f) {

    companion object {

        val ZERO = Vec4f(0f, 0f, 0f, 0f)
        val ONE = Vec4f(1f, 1f, 1f, 1f)

        val BLACK = Vec4f(0f, 0f, 0f, 1f)
        val WHITE = Vec4f(1f, 1f, 1f, 1f)

    }

    operator fun plus(other: Vec4f) = Vec4f(a + other.a, b + other.b, c + other.c, d + other.d)

    operator fun minus(other: Vec4f) = Vec4f(a - other.a, b - other.b, c - other.c, d - other.d)

    operator fun times(other: Vec4f) = Vec4f(a * other.a, b * other.b, c * other.c, d * other.d)

    operator fun div(other: Vec4f) = Vec4f(a / other.a, b / other.b, c / other.c, d / other.d)

    fun plus(r: Short, y: Short) = Vec4f(this.a + r, this.b + b, this.c + c, this.d + d)

    fun minus(r: Short, y: Short) = Vec4f(this.a - r, this.b - b, this.c - c, this.d - d)

    fun times(r: Short, y: Short) = Vec4f(this.a * r, this.b * b, this.c * c, this.d * d)

    fun div(r: Short, y: Short) = Vec4f(this.a / r, this.b / b, this.c / c, this.d / d)

    operator fun times(other: Int) = Vec4f(a * other, b * other, c * other, d * other)

    operator fun div(other: Int) = Vec4f(a / other, b / other, c / other, d / other)

    inline fun computeEach(block: (Float)->Float) = Vec4f(block(a), block(b), block(c), block(d))

}