package crpth.util.vec


data class Vec4f(val r: Float, val g: Float, val b: Float, val a: Float=1.0f) {

    companion object {

        val ZERO = Vec4f(0f, 0f, 0f, 0f)
        val ONE = Vec4f(1f, 1f, 1f, 1f)

        val BLACK = Vec4f(0f, 0f, 0f, 1f)
        val WHITE = Vec4f(1f, 1f, 1f, 1f)

    }

    operator fun plus(other: Vec4f) = Vec4f(r + other.r, g + other.g, b + other.b, a + other.a)

    operator fun minus(other: Vec4f) = Vec4f(r - other.r, g - other.g, b - other.b, a - other.a)

    operator fun times(other: Vec4f) = Vec4f(r * other.r, g * other.g, b * other.b, a * other.a)

    operator fun div(other: Vec4f) = Vec4f(r / other.r, g / other.g, b / other.b, a / other.a)

    fun plus(r: Short, y: Short) = Vec4f(this.r + r, this.g + g, this.b + b, this.a + a)

    fun minus(r: Short, y: Short) = Vec4f(this.r - r, this.g - g, this.b - b, this.a - a)

    fun times(r: Short, y: Short) = Vec4f(this.r * r, this.g * g, this.b * b, this.a * a)

    fun div(r: Short, y: Short) = Vec4f(this.r / r, this.g / g, this.b / b, this.a / a)

    operator fun times(other: Int) = Vec4f(r * other, g * other, b * other, a * other)

    operator fun div(other: Int) = Vec4f(r / other, g / other, b / other, a / other)

    inline fun computeEach(block: (Float)->Float) = Vec4f(block(r), block(g), block(b), block(a))

}