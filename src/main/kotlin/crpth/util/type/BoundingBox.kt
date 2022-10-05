package crpth.util.type

import crpth.util.vec.Vec2f

class BoundingBox(val posStart: Vec2f, val posEnd: Vec2f) {

    companion object {
        fun fromPosAndSize(pos: Vec2f, size: Vec2f) = BoundingBox(pos, pos + size)
    }

    operator fun contains(p: Vec2f) = posStart.x <= p.x && posStart.y <= p.y && p.x <= posEnd.x && p.y <= posEnd.y

    fun intersects(other: BoundingBox): Boolean {

        return contains(other.posStart) || contains(Vec2f(other.posEnd.x, other.posStart.y)) || contains(Vec2f(other.posStart.x, other.posEnd.y)) || contains(other.posEnd) ||
                other.contains(posStart) || other.contains(Vec2f(posEnd.x, posStart.y)) || other.contains(Vec2f(posStart.x, posEnd.y)) || other.contains(posEnd)

    }

}