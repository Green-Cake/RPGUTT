package crpth.rpgutt.entity

import crpth.rpgutt.RpgUtt
import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityPlayer(pos: GamePos, size: Vec2f, direction: Direction4) : EntityPerson("arrow", Vec2i(16, 16), pos, size, direction, "null") {

    companion object {

        fun decode(stream: DataInputStream): EntityPlayer {

            val pos = GamePos.readFrom(stream)

            val size = Vec2f.readFrom(stream)

            val direction = Direction4.values()[stream.readByte().resizeToInt()]

            return EntityPlayer(pos, size, direction)
        }

    }

    override val script get() = throw Exception()

    private fun getTalkableEntity(sceneMain: SceneMain) = sceneMain.entities.entities.firstOrNull {
        it !== this && it is IEntityTalkable && it is EntityObject &&
                if(direction == Direction4.NORTH || direction == Direction4.EAST)
                    it.intersects(pos.toVec2f()+direction.component.toVec2f(), size - direction.component.toVec2f()/4f)
                else
                    it.intersects(pos.toVec2f() + direction.component.toVec2f()/4f, size + direction.component.toVec2f()/4f)
    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(!sceneMain.playerCanMove)
            return IEntity.Feedback.CONTINUE

        if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {

            val target = getTalkableEntity(SceneMain) as IEntityTalkable?

            if(target != null)
                SceneMain.talk(target.getSerif(SceneMain) ?: run {
                    RpgUtt.logger.warn("No serif object returned!")
                    return IEntity.Feedback.CONTINUE
                })

            return IEntity.Feedback.CONTINUE
        }

        val amount = 1
        val cap = 7
        val strictCap = 1

        val flagW = Direction4.NORTH in RpgUtt.controller.directions
        val flagA = Direction4.WEST in RpgUtt.controller.directions
        val flagS = Direction4.SOUTH in RpgUtt.controller.directions
        val flagD = Direction4.EAST in RpgUtt.controller.directions

        if (flagW || flagS) {
            direction = if(flagW) Direction4.NORTH else Direction4.SOUTH
            val dist = if(flagW) pos.plusSub(0, amount) else pos.plusSub(0, -amount)

            if(SceneMain.canPlayerGoto(direction))
                pos = dist
            else for(i in 1..cap) {
                if(SceneMain.canEntityGoto(pos.plusSub(i, 0), direction)) {
                    pos = if(i <= strictCap) dist.plusSub(i, 0) else pos.plusSub(1, 0)
                    break
                }
                else if(SceneMain.canEntityGoto(pos.minusSub(i, 0), direction)) {
                    pos = if(i <= strictCap) dist.minusSub(i, 0) else pos.minusSub(1, 0)
                    break
                }
            }

        }

        if (flagA || flagD) {
            direction = if(flagD) Direction4.EAST else Direction4.WEST
            val dist = if(flagD) pos.plusSub(amount, 0) else pos.plusSub(-amount, 0)

            if(SceneMain.canPlayerGoto(direction))
                pos = dist
            else {

                for(i in 1..cap) {
                    if(SceneMain.canEntityGoto(pos.plusSub(0, i), direction)) {
                        pos = if(i <= strictCap) dist.plusSub(0, i) else pos.plusSub(0, 1)
                        break
                    }
                    else if(SceneMain.canEntityGoto(pos.minusSub(0, i), direction)) {
                        pos = if(i <= strictCap) dist.minusSub(0, i) else pos.minusSub(0, 1)
                        break
                    }
                }

            }

        }

        return IEntity.Feedback.CONTINUE
    }

    override fun encode(stream: DataOutputStream) {

        stream.writeLong(pos.data.data.toLong())
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)

    }

    override fun getSerif(sceneMain: SceneMain) = null

}