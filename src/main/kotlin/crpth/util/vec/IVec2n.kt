package crpth.util.vec

interface IVec2n<T> where T : Number, T : Comparable<T>{

    val x: T
    val y: T

    fun toVec2b() = Vec2b(x.toByte(), y.toByte())

    fun toVec2s() = Vec2s(x.toShort(), y.toShort())

    fun toVec2i() = Vec2i(x.toInt(), y.toInt())

    fun toVec2f() = Vec2f(x.toFloat(), y.toFloat())

    fun toVec2d() = Vec2d(x.toDouble(), y.toDouble())

    fun coerceIn(xRange: ClosedRange<T>, yRange: ClosedRange<T> = xRange): IVec2n<T>

}