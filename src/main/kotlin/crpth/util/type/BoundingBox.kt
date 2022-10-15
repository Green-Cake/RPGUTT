package crpth.util.type

import crpth.util.vec.IVec2n
import crpth.util.vec.Vec2d
import crpth.util.vec.Vec2f

class BoundingBox(val posStart: Vec2d, val posEnd: Vec2d) {

    companion object {
        fun fromPosAndSize(pos: Vec2d, size: Vec2d) = BoundingBox(pos, pos + size)

        fun fromPosAndSize(pos: IVec2n<*>, size: IVec2n<*>) = fromPosAndSize(pos.toVec2d(), size.toVec2d())

    }

    operator fun contains(p: Vec2d) = posStart.x <= p.x && posStart.y <= p.y && p.x <= posEnd.x && p.y <= posEnd.y

    operator fun contains(p: IVec2n<*>) = contains(p.toVec2d())

    fun intersects(other: BoundingBox): Boolean {

        return contains(other.posStart) || contains(Vec2d(other.posEnd.x, other.posStart.y)) || contains(Vec2d(other.posStart.x, other.posEnd.y)) || contains(other.posEnd) ||
                other.contains(posStart) || other.contains(Vec2d(posEnd.x, posStart.y)) || other.contains(Vec2d(posStart.x, posEnd.y)) || other.contains(posEnd)

    }

}