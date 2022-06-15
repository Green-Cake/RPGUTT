package crpth.util.vec

data class Vec4i(val r: Int, val g: Int, val b: Int, val a: Int) {

    companion object {

        val ZERO = Vec4i(0, 0, 0, 0)
        val ONE = Vec4i(1, 1, 1, 1)

        val BLACK = Vec4i(0, 0, 0, 255)
        val WHITE = Vec4i(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

    }

    operator fun plus(other: Vec4i) = Vec4i(r + other.r, g + other.g, b + other.b, a + other.a)

    operator fun minus(other: Vec4i) = Vec4i(r - other.r, g - other.g, b - other.b, a - other.a)

    operator fun times(other: Vec4i) = Vec4i(r * other.r, g * other.g, b * other.b, a * other.a)

    operator fun div(other: Vec4i) = Vec4i(r / other.r, g / other.g, b / other.b, a / other.a)

    fun plus(r: Int, g: Short, b: Short, a: Short) = Vec4i(this.r + r, this.g + g, this.b + b, this.a + a)

    fun minus(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.r - r, this.g - g, this.b - b, this.a - a)

    fun times(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.r * r, this.g * g, this.b * b, this.a * a)

    fun div(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.r / r, this.g / g, this.b / b, this.a / a)

    operator fun times(other: Int) = Vec4i(r * other, g * other, b * other, a * other)

    operator fun div(other: Int) = Vec4i(r / other, g / other, b / other, a / other)

    inline fun computeEach(block: (Int)->Int) = Vec4i(block(r), block(g), block(b), block(a))

}