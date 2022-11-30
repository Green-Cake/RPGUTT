@file: Suppress("NOTHING_TO_INLINE")

package crpth.util

import crpth.util.vec.*

inline fun vec(x: Int, y: Int) = Vec2i(x, y)
inline fun vec(x: Float, y: Float) = Vec2f(x, y)
inline fun vec(x: Double, y: Double) = Vec2d(x, y)

inline fun vec(x: Int, y: Int, z: Int) = Vec3i(x, y, z)
inline fun vec(x: Float, y: Float, z: Float) = Vec3f(x, y, z)
inline fun vec(x: Double, y: Double, z: Double) = Vec3d(x, y, z)