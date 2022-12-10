package crpth.util.vec

data class Vec4i(val a: Int, val b: Int, val c: Int, val d: Int) {

    companion object {

        val ZERO = Vec4i(0, 0, 0, 0)
        val ONE = Vec4i(1, 1, 1, 1)

        val BLACK = Vec4i(0, 0, 0, 255)
        val WHITE = Vec4i(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

    }

    operator fun plus(other: Vec4i) = Vec4i(a + other.a, b + other.b, c + other.c, d + other.d)

    operator fun minus(other: Vec4i) = Vec4i(a - other.a, b - other.b, c - other.c, d - other.d)

    operator fun times(other: Vec4i) = Vec4i(a * other.a, b * other.b, c * other.c, d * other.d)

    operator fun div(other: Vec4i) = Vec4i(a / other.a, b / other.b, c / other.c, d / other.d)

    fun plus(r: Int, g: Short, b: Short, a: Short) = Vec4i(this.a + r, this.b + g, this.c + b, this.d + a)

    fun minus(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.a - r, this.b - g, this.c - b, this.d - a)

    fun times(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.a * r, this.b * g, this.c * b, this.d * a)

    fun div(r: Short, g: Short, b: Short, a: Short) = Vec4i(this.a / r, this.b / g, this.c / b, this.d / a)

    operator fun times(other: Int) = Vec4i(a * other, b * other, c * other, d * other)

    operator fun div(other: Int) = Vec4i(a / other, b / other, c / other, d / other)

    inline fun computeEach(block: (Int)->Int) = Vec4i(block(a), block(b), block(c), block(d))

}