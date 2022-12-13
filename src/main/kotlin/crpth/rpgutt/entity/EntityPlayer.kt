package crpth.rpgutt.entity

import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.IEntityAI
import crpth.rpgutt.entity.ai.UpdateType
import crpth.rpgutt.scene.ISceneStage
import crpth.rpgutt.scene.SceneMain
import crpth.rpgutt.script.lib.Serif
import crpth.util.type.Direction
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityPlayer(pos: GamePos, size: Vec2f, direction: Direction) : EntityPerson("otaku01", Vec2i(32, 32), pos, size, direction, "null") {

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

    override val ai: IEntityAI = object : IEntityAI {

        override val updateType = UpdateType.ALWAYS

        override fun getSerif(params: EntityParams) = Serif.EMPTY

        override fun update(params: EntityParams) = Unit

        override fun init(params: EntityParams) = Unit

    }

    private fun getEntityToTalkWith(sceneMain: ISceneStage) = sceneMain.entities.childs.firstOrNull {
        it !== this && it is IEntityTalkable && it is EntityObject &&
                if(direction == Direction.NORTH || direction == Direction.EAST)
                    it.intersects(pos.toVec2f()+direction.component.toVec2f(), size - direction.component.toVec2f()/4f)
                else
                    it.intersects(pos.toVec2f() + direction.component.toVec2f()/4f, size + direction.component.toVec2f()/4f)
    }

    override fun update(sceneStage: ISceneStage): IEntity.Feedback {

        if(!sceneStage.canPlayerMove)
            return IEntity.Feedback.CONTINUE

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {

            val target = getEntityToTalkWith(SceneMain) as IEntityTalkable?

            if(target != null)
                SceneMain.talk(target.getSerif(SceneMain) ?: run {
                    RpgUtt.logger.warn("No serif object returned!")
                    return IEntity.Feedback.CONTINUE
                })

            return IEntity.Feedback.CONTINUE
        }

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_P)) {

            sceneStage.entities.requestAddEntity(EntityPerson("arrow", Vec2i(16, 16), pos, Vec2f(1f, 1f), Direction.SOUTH, "entity/test"))

        }

        val flagW = RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_W)
        val flagA = RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_A)
        val flagS = RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_S)
        val flagD = RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_D)

        val amount = if((flagW || flagS) && (flagA || flagD)) 11 else 16
        val cap = 112
        val strictCap = 16

        run y_side@ {
            if (flagW || flagS) {
                direction = if (flagW) Direction.NORTH else Direction.SOUTH
                val dist = if (flagW) GamePos.sub(0, 1) else GamePos.sub(0, -1)

                repeat(amount) {

                    if (!SceneMain.canPlayerGoto(direction)) {

                        val maybeMovable = SceneMain.entities.childs.filterIsInstance<EntityObject>().filter {
                            it != this && it.intersects(
                                this.pos.toVec2f() + direction.component.toVec2f() / GamePos.BITS_FOR_SUBPIXEL.toFloat(),
                                this.size
                            )
                        }

                        if (maybeMovable.isNotEmpty() && maybeMovable.all {
                                it is EntityMovable && sceneStage.canEntityGoto(
                                    it,
                                    direction
                                )
                            }) {

                            maybeMovable.forEach {
                                it.pos += GamePos.sub(direction.component.x, direction.component.y)
                            }

                        } else {

                            return@y_side

                        }

                    }

                    pos += dist
                }
            }
        }

        run x_side@ {
            if (flagA || flagD) {
                direction = if(flagA) Direction.WEST else Direction.EAST
                val dist = if(flagD) GamePos.sub(1, 0) else GamePos.sub(-1, 0)

                repeat(amount) {

                    if(!SceneMain.canPlayerGoto(direction)) {

                        val maybeMovable = SceneMain.entities.childs.filterIsInstance<EntityObject>().filter {
                            it != this && it.intersects(this.pos.toVec2f() + direction.component.toVec2f() / GamePos.BITS_FOR_SUBPIXEL.toFloat(), this.size)
                        }

                        if(maybeMovable.isNotEmpty() && maybeMovable.all { it is EntityMovable && sceneStage.canEntityGoto(it, direction) }) {

                            maybeMovable.forEach {
                                it.pos += GamePos.sub(direction.component.x, direction.component.y)
                            }

                        } else {

                            return@x_side

                        }

                    }

                    pos += dist
                }

            }
        }

        return IEntity.Feedback.CONTINUE
    }

    override fun encode(stream: DataOutputStream) {

        stream.writeInt(pos.x)
        stream.writeInt(pos.y)
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)

    }

    override fun getSerif(sceneMain: ISceneStage) = null

}