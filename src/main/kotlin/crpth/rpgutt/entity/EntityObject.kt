package crpth.rpgutt.entity

import crpth.util.vec.GamePos
import crpth.util.vec.Vec2f

abstract class EntityObject(open var pos: GamePos, open var size: Vec2f) : EntityWithRendering() {

    override val posZ: Int
        get() = pos.z

    fun intersects(p: Vec2f, s: Vec2f): Boolean {

        val p0 = pos.toVec2f()
        val p0a = p0 + size
        val p1 = p
        val p1a = p1 + s

        return ((p0.x <= p1.x && p1.x <= p0a.x) && (p0.y <= p1.y && p1.y <= p0a.y))
                || ((p0.x <= p1.x && p1.x <= p0a.x) && (p0.y <= p1a.y && p1a.y <= p0a.y))
                || ((p0.x <= p1a.x && p1a.x <= p0a.x) && (p0.y <= p1.y && p1.y <= p0a.y))
                || ((p0.x <= p1a.x && p1a.x <= p0a.x) && (p0.y <= p1a.y && p1a.y <= p0a.y))

    }

    fun intersects(target: EntityObject, considerZ: Boolean = false): Boolean {

        if(considerZ && pos.z != target.pos.z)
            return false

        return intersects(target.pos.toVec2f(), target.size)

    }

}