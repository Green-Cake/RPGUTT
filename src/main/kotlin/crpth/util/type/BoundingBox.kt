package crpth.util.type

import crpth.util.vec.Vec2f

class BoundingBox(val posStart: Vec2f, val posEnd: Vec2f) {

    companion object {
        fun fromPosAndSize(pos: Vec2f, size: Vec2f) = BoundingBox(pos, pos + size)
    }

    operator fun contains(p: Vec2f) = posStart.x <= p.x && posStart.y <= p.y && p.x <= posEnd.x && p.y <= posEnd.y

}