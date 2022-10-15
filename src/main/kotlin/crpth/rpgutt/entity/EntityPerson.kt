package crpth.rpgutt.entity

import crpth.rpgutt.ResourceManager
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.ai.IEntityAI
import crpth.rpgutt.scene.SceneMain
import crpth.rpgutt.script.ScriptEvaluator
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.UpdateType
import crpth.rpgutt.entity.ai.UpdateType.*
import crpth.rpgutt.script.lib.Serif
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.type.BoundingBox
import crpth.util.vec.*
import org.lwjgl.opengl.GL11
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.script.experimental.api.CompiledScript
import kotlin.script.experimental.host.StringScriptSource

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

    lateinit var ai: IEntityAI
        private set

    var motionCounter = 0
    var motion = Vec2i.ZERO
    var speed = 1

    val textures by TileSet.createLazyInit("assets/rpgutt/textures/entity/$name.png", sizePerTile)

    open val script: CompiledScript by lazy {
        try {

            if(scriptSrcPath == "null") {
                RpgUtt.logger.warn("Script file not specified! (entity: $name)")
                throw Exception()
            } else {
                ScriptEvaluator.compile(StringScriptSource(ResourceManager.loadScriptSrc(scriptSrcPath)))
            }
        } catch (t: Throwable) {
            throw t
        }
    }

    fun resetMotion() {
        motion = Vec2i.ZERO
    }

    fun move(d: Direction=direction, amount: Int=1) {
        motion += d.component*amount
    }

    var evaluated = false

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(!::ai.isInitialized && this !is EntityPlayer) {
            ai = ScriptEvaluator.eval<Any?>(EntityParams(sceneMain, this, "update"), script) as IEntityAI
        }

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
                    pos = pos.plusSub(speed, 0)
            } else if(motion.x < 0) {
                motion += Vec2i(speed, 0)
                if(sceneMain.canEntityGoto(pos, Direction.WEST))
                    pos = pos.minusSub(speed, 0)
            }

            if(motion.y > 0) {
                motion -= Vec2i(0, speed)
                if(sceneMain.canEntityGoto(pos, Direction.NORTH))
                    pos = pos.plusSub(0, speed)
            } else if(motion.y < 0) {
                motion += Vec2i(0, speed)
                if(sceneMain.canEntityGoto(pos, Direction.SOUTH))
                    pos = pos.minusSub(0, speed)
            }

        }

        if(!sceneMain.isTalking)
//            ScriptEvaluator.eval<Any?>(ScriptImplicitReceiver(sceneMain, this, "update"), script)
            ai.update(EntityParams(sceneMain, this@EntityPerson, "update"))

        return IEntity.Feedback.CONTINUE
    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        if(!doRender(sceneMain, renderer, sceneMain.renderingBound))
            return

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        renderer.renderTexture(textures[direction.ordinal], sceneMain.getActualPos(pos), sceneMain.getActualSize(size))

    }

    override fun doRender(sceneMain: SceneMain, renderer: Renderer, bound: BoundingBox): Boolean {

        val margin = Vec2f(3.0f, 3.0f)

        val self = BoundingBox.fromPosAndSize(pos.toVec2f(), size.toVec2f())
        val self_m = BoundingBox.fromPosAndSize(pos.toVec2f() - margin/2.0f, size.toVec2f() + margin)

        if(!::ai.isInitialized && this !is EntityPlayer) {
            ai = ScriptEvaluator.eval<Any?>(EntityParams(sceneMain, this, "update"), script) as IEntityAI
        }

        return if(this is EntityPlayer) true else when(ai.updateType) {
            ALWAYS -> true
            WHEN_RENDERED -> bound.intersects(self)
            WHEN_RENDERED_WIDER -> bound.intersects(self_m)
        }

    }

    override fun encode(stream: DataOutputStream) {

        stream.writeString(name)
        stream.writeLong(sizePerTile.data.toLong())
        stream.writeLong(pos.data.data.toLong())
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)
        stream.writeString(scriptSrcPath)

    }

    override fun getSerif(sceneMain: SceneMain): Serif? {

        return ai.getSerif(EntityParams(sceneMain, this@EntityPerson, "serif"))

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