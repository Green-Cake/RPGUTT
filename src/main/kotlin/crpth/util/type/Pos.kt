package crpth.util.type

import crpth.util.vec.IVec2nWrapper
import crpth.util.vec.Vec2f
import crpth.util.vec.atan2

@JvmInline
value class Pos(override val value: Vec2f) : IVec2nWrapper<Float> {

    fun lookTo(target: Pos): Double {

        val dif = target.value - this.value

        return atan2(dif).value / Math.PI * 180

    }

}

@Suppress("NOTHING_TO_INLINE")
inline fun Vec2f.pos() = Pos(this)