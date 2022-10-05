package crpth.util.vec

@JvmInline
value class Vec4b(val data: UInt) {

    companion object {

        val ZERO = Vec4b(0, 0, 0, 0)
        val ONE = Vec4b(1, 1, 1, 1)

        val BLACK = Vec4b(0, 0, 0, 255)
        val WHITE = Vec4b(255, 255, 255, 255)

        val RED = Vec4b(255, 0, 0, 255)
        val GREEN = Vec4b(0, 255, 0, 255)
        val BLUE = Vec4b(0, 0, 255, 255)

    }

    val r: Byte
        get() = (data shr 24).toByte()
    val g: Byte
        get() = (data shr 16).toByte()
    val b: Byte
        get() = (data shr 8).toByte()
    val a: Byte
        get() = data.toByte()

    constructor(r: Byte, g: Byte, b: Byte, a: Byte=255.toByte()) : this(
        (r.toUInt() shl 24) + (g.resizeToUInt() shl 16) + (b.resizeToUInt() shl 8) + a.resizeToUInt()
    )

    constructor(r: Int, g: Int, b: Int, a: Int=255) : this(r.toByte(), g.toByte(), b.toByte(), a.toByte())

    operator fun plus(other: Vec4b) = Vec4b(r + other.r, g + other.g, b + other.b, a + other.a)

    operator fun minus(other: Vec4b) = Vec4b(r - other.r, g - other.g, b - other.b, a - other.a)

    operator fun times(other: Vec4b) = Vec4b(r * other.r, g * other.g, b * other.b, a * other.a)

    operator fun div(other: Vec4b) = Vec4b(r / other.r, g / other.g, b / other.b, a / other.a)

    fun plus(r: Byte, g: Byte, b: Byte, a: Byte) = Vec4b(this.r + r, this.g + g, this.b + b, this.a + a)

    fun minus(r: Byte, g: Byte, b: Byte, a: Byte) = Vec4b(this.r - r, this.g - g, this.b - b, this.a - a)

    fun times(r: Byte, g: Byte, b: Byte, a: Byte) = Vec4b(this.r * r, this.g * g, this.b * b, this.a * a)

    fun div(r: Byte, g: Byte, b: Byte, a: Byte) = Vec4b(this.r / r, this.g / g, this.b / b, this.a / a)

    operator fun times(other: Int) = Vec4b(r * other, g * other, b * other, a * other)

    operator fun div(other: Int) = Vec4b(r / other, g / other, b / other, a / other)

    override fun toString() = "(${r.toUByte()}, ${g.toUByte()}, ${b.toUByte()}, ${a.toUByte()})"

    operator fun component1() = r

    operator fun component2() = g

    operator fun component3() = b

    operator fun component4() = a

    inline fun computeEach(block: (Byte)->Byte) = Vec4b(block(r), block(g), block(b), block(a))

    fun toVec4i() = Vec4i(r.toInt(), g.toInt(), b.toInt(), a.toInt())

    fun withAlpha(alpha: Byte) = Vec4b(r, g, b, alpha)

}