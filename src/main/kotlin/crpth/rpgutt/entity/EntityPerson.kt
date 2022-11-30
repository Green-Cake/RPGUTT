package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.UpdateType.*
import crpth.rpgutt.script.AIManager
import crpth.rpgutt.script.lib.Serif
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.type.BoundingBox
import crpth.util.vec.*
import org.lwjgl.opengl.GL11
import java.io.DataInputStream
import java.io.DataOutputStream

open class EntityPerson(val name: String, val sizePerTile: Vec2i, pos: GamePos, size: Vec2f, var direction: Direction, val scriptSrcPath: String) : EntityObject(pos, size), IEntityTalkable {

    companion object {

        fun decode(stream: DataInputStream): EntityPerson {

            val name = stream.readString()
            val sizePer = Vec2i.readFrom(stream)

            val pos = GamePos.readFrom(stream)

            val size = Vec2f.readFrom(stream)

            val direction = Direction.values()[stream.readByte().resizeToInt()]

            val scriptSrc = stream.readString()

            return EntityPerson(name, sizePer, pos, size, direction, scriptSrc)
        }

    }

    open val ai get() = AIManager.load(this, scriptSrcPath)

    var motionCounter = 0
    var motion = Vec2i.ZERO
    var speed = 1

    val textures by TileSet.createLazyInit("assets/rpgutt/textures/entity/$name.png", sizePerTile)

    fun resetMotion() {
        motion = Vec2i.ZERO
    }

    fun move(d: Direction=direction, amount: Int=1) {
        motion += d.component*amount
    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        val doUpdate = when(ai.updateType) {
            ALWAYS -> {
                true
            }
            WHEN_RENDERED -> {
                true
            }
            WHEN_RENDERED_WIDER -> true
        }

        if(!doUpdate)
            return IEntity.Feedback.CONTINUE

        if(motionCounter == 0 && !sceneMain.isTalking) {

            if(motion.x > 0) {
                motion -= Vec2i(speed, 0)
                if(sceneMain.canEntityGoto(pos, Direction.EAST))
                    pos = pos.plus(speed, 0)
            } else if(motion.x < 0) {
                motion += Vec2i(speed, 0)
                if(sceneMain.canEntityGoto(pos, Direction.WEST))
                    pos = pos.minus(speed, 0)
            }

            if(motion.y > 0) {
                motion -= Vec2i(0, speed)
                if(sceneMain.canEntityGoto(pos, Direction.NORTH))
                    pos = pos.plus(0, speed)
            } else if(motion.y < 0) {
                motion += Vec2i(0, speed)
                if(sceneMain.canEntityGoto(pos, Direction.SOUTH))
                    pos = pos.minus(0, speed)
            }

        }

        if(!sceneMain.isTalking)
            ai.update(EntityParams(this@EntityPerson, "update"))

        return IEntity.Feedback.CONTINUE
    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        if(!isRenderingTarget(sceneMain, renderer, sceneMain.renderingBound))
            return

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        renderer.renderTexture(textures[direction.ordinal], sceneMain.getActualPos(pos), sceneMain.getActualSize(size))

    }

    override fun isRenderingTarget(sceneMain: SceneMain, renderer: Renderer, bound: BoundingBox): Boolean {

        val margin = Vec2f(3.0f, 3.0f)

        val self = BoundingBox.fromPosAndSize(pos.toVec2f(), size.toVec2f())
        val self_m = BoundingBox.fromPosAndSize(pos.toVec2f() - margin/2.0f, size.toVec2f() + margin)

        return if(this is EntityPlayer) true else when(ai.updateType) {
            ALWAYS -> true
            WHEN_RENDERED -> bound.intersects(self)
            WHEN_RENDERED_WIDER -> bound.intersects(self_m)
        }

    }

    override fun encode(stream: DataOutputStream) {

        stream.writeString(name)
        stream.writeLong(sizePerTile.data.toLong())
        pos.data.encode(stream)
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)
        stream.writeString(scriptSrcPath)

    }

    override fun getSerif(sceneMain: SceneMain): Serif? {

        return ai.getSerif(EntityParams(this@EntityPerson, "serif"))

//        return ScriptEvaluator.eval<Serif>(ScriptImplicitReceiver(sceneMain, this, "serif"), script ?: return null)

    }

    override fun isTalkable(sceneMain: SceneMain, player: EntityPlayer) = this !is EntityPlayer

    fun turnRight() {
        direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
    }

    fun turnLeft() {
        val i = (direction.ordinal - 1) % Direction.values().size
        direction = Direction.values()[if(i < 0) Direction.values().size + i else i]
    }

}