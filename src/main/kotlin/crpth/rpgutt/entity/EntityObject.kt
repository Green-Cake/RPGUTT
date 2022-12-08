package crpth.rpgutt.entity

import crpth.util.vec.GamePos
import crpth.util.vec.Vec2f

abstract class EntityObject(open var pos: GamePos, open var size: Vec2f) : EntityWithRendering() {

    /**
     * The smaller the value is, the earlier it will be rendered.
     */
    override val posZ = -1

    /**
     * @param position the position of the rectangle to check with.
     * @param size the size of the rectangle to check with.
     * @return true if this object intersects the rectangle with the specified position and scale
     */
    fun intersects(position: Vec2f, size: Vec2f): Boolean {

        val p0 = pos.toVec2f()
        val p0a = p0 + this.size
        val p1 = position
        val p1a = p1 + size

        return ((p0.x <= p1.x && p1.x <= p0a.x) && (p0.y <= p1.y && p1.y <= p0a.y))
                || ((p0.x <= p1.x && p1.x <= p0a.x) && (p0.y <= p1a.y && p1a.y <= p0a.y))
                || ((p0.x <= p1a.x && p1a.x <= p0a.x) && (p0.y <= p1.y && p1.y <= p0a.y))
                || ((p0.x <= p1a.x && p1a.x <= p0a.x) && (p0.y <= p1a.y && p1a.y <= p0a.y))

    }

    /**
     * @param target The target entity to check with.
     * @param considerZ If specified as true, return false if they don't have the same Z position.
     * @return true if this object intersects the rectangle of the target entity.
     */
    fun intersects(target: EntityObject, considerZ: Boolean = false): Boolean {

        if(considerZ && target.posZ != target.posZ)
            return false

        return intersects(target.pos.toVec2f(), target.size)

    }

}