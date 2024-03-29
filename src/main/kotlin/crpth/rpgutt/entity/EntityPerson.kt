package crpth.rpgutt.entity

import crpth.rpgutt.scene.ISceneStage
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.UpdateType.*
import crpth.rpgutt.script.AIManager
import crpth.rpgutt.script.lib.Serif
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.type.BoundingBox
import crpth.util.type.Direction
import crpth.util.vec.*
import java.io.DataInputStream
import java.io.DataOutputStream

open class EntityPerson(val textureName: String, val sizePerTile: Vec2i, pos: GamePos, size: Vec2f, var direction: Direction, val scriptSrcPath: String) : EntityObject(pos, size), IEntityTalkable {

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

    open val ai by lazy { AIManager.load(scriptSrcPath) }

    var motionCounter = 0
    var motion = Vec2i.ZERO
    var speed = 1

    val textures by TileSet.createLazyInit("assets/rpgutt/textures/entity/$textureName.png", sizePerTile)

    fun resetMotion() {
        motion = Vec2i.ZERO
    }

    fun move(d: Direction =direction, amount: Int=1) {
        motion += d.component*amount
    }

    private var isInitialized = false

    override fun update(sceneStage: ISceneStage): IEntity.Feedback {

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

        if(!isInitialized) {
            ai.init(EntityParams(this))
            isInitialized = true
        }

        if(motionCounter == 0 && !sceneStage.isTalking) {

            if(motion.x > 0) {
                motion -= Vec2i(speed, 0)
                repeat(speed) {
                    if(sceneStage.canEntityGoto(this, Direction.EAST))
                        pos = pos.plus(1, 0)
                }
            } else if(motion.x < 0) {
                motion += Vec2i(speed, 0)
                repeat(speed) {
                    if(sceneStage.canEntityGoto(this, Direction.WEST))
                        pos = pos.minus(1, 0)
                }
            }

            if(motion.y > 0) {
                motion -= Vec2i(0, speed)
                repeat(speed) {
                    if(sceneStage.canEntityGoto(this, Direction.NORTH))
                        pos = pos.plus(0, 1)
                }
            } else if(motion.y < 0) {
                motion += Vec2i(0, speed)
                repeat(speed) {
                    if(sceneStage.canEntityGoto(this, Direction.SOUTH))
                        pos = pos.minus(0, 1)
                }
            }

        }

        if(!sceneStage.isTalking)
            ai.update(EntityParams(this@EntityPerson))

        return IEntity.Feedback.CONTINUE
    }

    var tick = 0

    override fun render(sceneMain: ISceneStage, renderer: Renderer) {

//        if(!isRenderingTarget(sceneMain, renderer, sceneMain.renderingBound))
//            return
        //TODO

        tick = (tick+1) % 60

        renderer.renderTexture(textures[direction.ordinal*3 + (tick/15).let { if(it == 3) 1 else it }], sceneMain.getActualPos(pos), sceneMain.getActualSize(size))

    }

    override fun isRenderingTarget(sceneMain: ISceneStage, renderer: Renderer, bound: BoundingBox): Boolean {

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

        stream.writeString(textureName)
        stream.writeLong(sizePerTile.data.toLong())
        pos.encode(stream)
        stream.writeLong(size.data.toLong())
        stream.writeByte(direction.ordinal)
        stream.writeString(scriptSrcPath)

    }

    override fun getSerif(sceneMain: ISceneStage): Serif? {

        return ai.getSerif(EntityParams(this@EntityPerson))

//        return ScriptEvaluator.eval<Serif>(ScriptImplicitReceiver(sceneMain, this, "serif"), script ?: return null)

    }

    override fun isTalkable(sceneMain: ISceneStage, player: EntityPlayer) = this !is EntityPlayer

    fun turnRight() {
        direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
    }

    fun turnLeft() {
        val i = (direction.ordinal - 1) % Direction.values().size
        direction = Direction.values()[if(i < 0) Direction.values().size + i else i]
    }

}