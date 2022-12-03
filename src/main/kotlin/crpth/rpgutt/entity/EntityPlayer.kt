package crpth.rpgutt.entity

import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.ai.IEntityAI
import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityPlayer(pos: GamePos, size: Vec2f, direction: Direction) : EntityPerson("arrow", Vec2i(16, 16), pos, size, direction, "null") {

    companion object {

        fun decode(stream: DataInputStream): EntityPlayer {

            val pos = GamePos.readFrom(stream)

            val size = Vec2f.readFrom(stream)

            val direction = Direction.values()[stream.readByte().resizeToInt()]

            return EntityPlayer(pos, size, direction)
        }

    }

    init {
        speed = 16
    }

    override val ai: IEntityAI
        get() = throw Exception()

    private fun getEntityToTalkWith(sceneMain: SceneMain) = sceneMain.entities.childs.firstOrNull {
        it !== this && it is IEntityTalkable && it is EntityObject &&
                if(direction == Direction.NORTH || direction == Direction.EAST)
                    it.intersects(pos.toVec2f()+direction.component.toVec2f(), size - direction.component.toVec2f()/4f)
                else
                    it.intersects(pos.toVec2f() + direction.component.toVec2f()/4f, size + direction.component.toVec2f()/4f)
    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(!sceneMain.playerCanMove)
            return IEntity.Feedback.CONTINUE

        if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {

            val target = getEntityToTalkWith(SceneMain) as IEntityTalkable?

            if(target != null)
                SceneMain.talk(target.getSerif(SceneMain) ?: run {
                    RpgUtt.logger.warn("No serif object returned!")
                    return IEntity.Feedback.CONTINUE
                })

            return IEntity.Feedback.CONTINUE
        }

        if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_P)) {

            sceneMain.entities.requestAddEntity(EntityPerson("arrow", Vec2i(16, 16), pos, Vec2f(1f, 1f), Direction.SOUTH, "entity/test"))

        }

        val flagW = RpgUtt.isKeyDown(GLFW.GLFW_KEY_W)
        val flagA = RpgUtt.isKeyDown(GLFW.GLFW_KEY_A)
        val flagS = RpgUtt.isKeyDown(GLFW.GLFW_KEY_S)
        val flagD = RpgUtt.isKeyDown(GLFW.GLFW_KEY_D)

        val amount = if((flagW || flagS) && (flagA || flagD)) 11 else 16
        val cap = 112
        val strictCap = 16

        run y_side@ {
            if (flagW || flagS) {
                direction = if(flagW) Direction.NORTH else Direction.SOUTH
                val dist = if(flagW) GamePos.sub(0, 1) else GamePos.sub(0, -1)

                var i = amount
                while(i != 0) {

                    if(!SceneMain.canPlayerGoto(direction))
                        return@y_side

                    pos += dist
                    --i
                }

//            if(SceneMain.canPlayerGoto(direction))
//                pos = pos + dist
//            else for(i in 1..cap) {
//                if(SceneMain.canEntityGoto(pos.plus(i, 0), direction)) {
//                    pos = if(i <= strictCap) dist.plus(i, 0) else pos.plus(1, 0)
//                    break
//                }
//                else if(SceneMain.canEntityGoto(pos.minus(i, 0), direction)) {
//                    pos = if(i <= strictCap) dist.minus(i, 0) else pos.minus(1, 0)
//                    break
//                }
//            }

            }
        }

        run x_side@ {
            if (flagA || flagD) {
                direction = if(flagA) Direction.WEST else Direction.EAST
                val dist = if(flagD) GamePos.sub(1, 0) else GamePos.sub(-1, 0)

                var i = amount
                while(i != 0) {

                    if(!SceneMain.canPlayerGoto(direction))
                        return IEntity.Feedback.CONTINUE

                    pos += dist
                    --i
                }

//            if(SceneMain.canPlayerGoto(direction))
//                pos = pos + dist
//            else for(i in 1..cap) {
//                if(SceneMain.canEntityGoto(pos.plus(i, 0), direction)) {
//                    pos = if(i <= strictCap) dist.plus(i, 0) else pos.plus(1, 0)
//                    break
//                }
//                else if(SceneMain.canEntityGoto(pos.minus(i, 0), direction)) {
//                    pos = if(i <= strictCap) dist.minus(i, 0) else pos.minus(1, 0)
//                    break
//                }
//            }

            }
        }

//        if (flagA || flagD) {
//            direction = if(flagD) Direction.EAST else Direction.WEST
//            val dist = if(flagD) pos.plus(amount, 0) else pos.plus(-amount, 0)
//
//            if(SceneMain.canPlayerGoto(direction))
//                pos = dist
//            else {
//
//                for(i in 1..cap) {
//                    if(SceneMain.canEntityGoto(pos.plus(0, i), direction)) {
//                        pos = if(i <= strictCap) dist.plus(0, i) else pos.plus(0, 1)
//                        break
//                    }
//                    else if(SceneMain.canEntityGoto(pos.minus(0, i), direction)) {
//                        pos = if(i <= strictCap) dist.minus(0, i) else pos.minus(0, 1)
//                        break
//                    }
//                }
//
//            }
//
//        }

        return IEntity.Feedback.CONTINUE
    }

    override fun encode(stream: DataOutputStream) {

        stream.writeInt(pos.data.x)
        stream.writeInt(pos.data.y)
        stream.writeInt(pos.data.z)
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)

    }

    override fun getSerif(sceneMain: SceneMain) = null

}