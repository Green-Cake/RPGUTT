@file: Suppress("NOTHING_TO_INLINE")

package crpth.util.vec

import crpth.util.type.Radian
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.math.atan2

typealias IVec2f = IVec2n<Float>

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2b.Companion.readFrom(stream: DataInputStream) = Vec2b(stream.readShort().toUShort())

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2s.Companion.readFrom(stream: DataInputStream) = Vec2s(stream.readInt().toUInt())

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2i.Companion.readFrom(stream: DataInputStream) = Vec2i(stream.readLong().toULong())

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2f.Companion.readFrom(stream: DataInputStream) = Vec2f(stream.readLong().toULong())

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2d.Companion.readFrom(stream: DataInputStream) = Vec2d(stream.readDouble(), stream.readDouble())

@Suppress("NOTHING_TO_INLINE")
inline fun Vec4b.Companion.readFrom(stream: DataInputStream) = Vec4b(stream.readInt().toUInt())

@Suppress("NOTHING_TO_INLINE")
inline fun DataInputStream.readString(): String {

    val length = readShort().resizeToInt()
    return String(ByteArray(length) { readByte() })

}

@Suppress("NOTHING_TO_INLINE")
inline fun Byte.resizeToUInt() = toUInt() and 0xFFu

@Suppress("NOTHING_TO_INLINE")
inline fun Short.resizeToUInt() = toUInt() and 0xFF_FFu

@Suppress("NOTHING_TO_INLINE")
inline fun Short.resizeToULong() = toULong() and 0xFF_FFu

@Suppress("NOTHING_TO_INLINE")
inline fun Int.resizeToULong() = toULong() and 0xFFFF_FFFFu

@Suppress("NOTHING_TO_INLINE")
inline fun Byte.resizeToInt() = resizeToUInt().toInt()

@Suppress("NOTHING_TO_INLINE")
inline fun Short.resizeToInt() = resizeToUInt().toInt()

@Suppress("NOTHING_TO_INLINE")
inline fun Int.resizeToLong() = resizeToULong().toLong()

@Suppress("NOTHING_TO_INLINE")
inline fun DataOutputStream.writeString(str: String) {
    val bytes = str.toByteArray()
    writeShort(bytes.size)
    write(bytes)
}

//

fun Vec3f.lookTo(target: Vec3f): Vec3f {

    val dif = target - this

    return Vec3f(atan2(dif.y, dif.z), atan2(dif.z, dif.x), atan2(dif.y, dif.x))/Math.PI.toFloat()*180f

}

fun atan2(vec: IVec2n<*>): Radian {
    val v = vec.toVec2d()
    return Radian.of(atan2(v.y, v.x))
}

inline fun vec(x: Int, y: Int) = Vec2i(x, y)
inline fun vec(x: Float, y: Float) = Vec2f(x, y)
inline fun vec(x: Double, y: Double) = Vec2d(x, y)

inline fun vec(x: Int, y: Int, z: Int) = Vec3i(x, y, z)
inline fun vec(x: Float, y: Float, z: Float) = Vec3f(x, y, z)
inline fun vec(x: Double, y: Double, z: Double) = Vec3d(x, y, z)
