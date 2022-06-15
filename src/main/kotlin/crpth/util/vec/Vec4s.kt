package crpth.util.vec

@JvmInline
value class Vec4s(val data: ULong) {

    companion object {

        val ZERO = Vec4s(0, 0, 0, 0)
        val ONE = Vec4s(1, 1, 1, 1)

    }

    val x: Short
        get() = (data shr 48).toShort()

    val y: Short
        get() = (data shr 32).toShort()

    val z: Short
        get() = (data shr 16).toShort()

    val w: Short
        get() = data.toShort()

    constructor(x: Short, y: Short, z: Short, w: Short) : this(
        (x.resizeToULong() shl 48) + (y.resizeToULong() shl 32) + (z.resizeToULong() shl 16) + w.resizeToULong()
    )

    constructor(r: Int, g: Int, b: Int, a: Int) : this(r.toShort(), g.toShort(), b.toShort(), a.toShort())

    operator fun plus(other: Vec4s) = Vec4s(x + other.x, y + other.y, z + other.z, w + other.w)

    operator fun minus(other: Vec4s) = Vec4s(x - other.x, y - other.y, z - other.z, w - other.w)

    operator fun times(other: Vec4s) = Vec4s(x * other.x, y * other.y, z * other.z, w * other.w)

    operator fun div(other: Vec4s) = Vec4s(x / other.x, y / other.y, z / other.z, w / other.w)

    fun plus(x: Short, y: Short, z: Short, w: Short) = Vec4s(this.x + x, this.y + y, this.z + z, this.w + w)

    fun minus(x: Short, y: Short, z: Short, w: Short) = Vec4s(this.x - x, this.y - y, this.z - z, this.w - w)

    fun times(x: Short, y: Short, z: Short, w: Short) = Vec4s(this.x * x, this.y * y, this.z * z, this.w * w)

    fun div(x: Short, y: Short, z: Short, w: Short) = Vec4s(this.x / x, this.y / y, this.z / z, this.w / w)

    operator fun times(other: Int) = Vec4s(x * other, y * other, z * other, w * other)

    operator fun div(other: Int) = Vec4s(x / other, y / other, z / other, w / other)

    operator fun unaryMinus() = times(-1)

    override fun toString() = "(${x.toUShort()}, ${y.toUShort()}, ${z.toUShort()}, ${w.toUShort()})"

    operator fun component1() = x

    operator fun component2() = y

    operator fun component3() = z

    operator fun component4() = w

}