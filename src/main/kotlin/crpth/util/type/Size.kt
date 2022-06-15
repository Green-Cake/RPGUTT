package crpth.util.type

import crpth.util.vec.IVec2nWrapper
import crpth.util.vec.Vec2f

@JvmInline
value class Size(override val value: Vec2f) : IVec2nWrapper<Float> {

}

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2f.size() = Pos(this)