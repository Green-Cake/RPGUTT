package crpth.rpgutt.scene

import crpth.rpgutt.DEBUG_MODE
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.*
import crpth.rpgutt.map.TileMap
import crpth.rpgutt.map.TileMapLoader
import crpth.rpgutt.scene.SceneMain.Parameter.*
import crpth.rpgutt.script.lib.Serif
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.type.BoundingBox
import crpth.util.vec
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.ceil

object SceneMain : IScene {

    var rotation = Vec3f(0.0f, 0.0f, 0.0f)

    val tileSet by TileSet.createLazyInit("assets/rpgutt/textures/tile/tiles0.png", Vec2i(16, 16))

    val barrierTiles = setOf<UShort>(
        0u, 1u, 2u
    )

    lateinit var map: TileMap

    lateinit var entitiesPre: EntityParallel

    lateinit var entities: EntityParallel

    val zoomValues = listOf(0.05, 0.08, 0.1, 0.12)

    var scale = zoomValues[1]

    val renderingBound: BoundingBox get() {

        val numX = 1.0/scale / 3 * 4
        val numY = numX / 4 * 3

        return BoundingBox.fromPosAndSize(getScroll(), Vec2d(numX, numY))

    }

    private val parameters = values().associateWithTo(EnumMap(Parameter::class.java)) {

        when(it) {
            RENDER_TILES -> Parameter.TRUE
            RENDER_ENTITIES -> Parameter.TRUE
            TILE_ID_VOID -> 0u
        }

    }

    var statusPre = IEntity.Feedback.CONTINUE

    var isTalking = false

    var currentSerifEntity: EntitySerif? = null

    val player: EntityPlayer get() = entities.childs.filterIsInstance<EntityPlayer>().first()

    val playerCanMove get() = !isTalking

    var isZooming = false
    var zoomTarget = 0.0
    var zoomSpeed = 0.002

    fun setParam(p: Parameter, value: UInt) {
        parameters[p] = value
    }

    fun getParam(p: Parameter) = parameters.getOrDefault(p, 0u)

    fun loadNewMap(name: String) {

        statusPre = IEntity.Feedback.CONTINUE

        map = TileMapLoader.load(name)!!
        entitiesPre = EntityManager.createEntity(map.entityFactories[0]) as EntityParallel
        entities = EntityManager.createEntity(map.entityFactories[1]) as EntityParallel

    }

    var isLoadingFinished = false

    override fun init() {

        RpgUtt.logger.info("SceneMain init start")

        thread {
            RpgUtt.logger.info("Map loading start")
            loadNewMap("test")
            RpgUtt.logger.info("Map loading finished")
            isLoadingFinished = true
        }

        GL11.glViewport(0, 0, RpgUtt.windowSize.x, RpgUtt.windowSize.y)

    }

    fun startZoom(value: Double) {
        isZooming = true
        zoomTarget = value
    }

    override fun update() {

        if(!isLoadingFinished)
            return

        if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            startZoom(zoomValues[(zoomValues.indexOf(zoomValues.minBy { abs(scale - it) })+1).coerceIn(0..zoomValues.lastIndex)])
        } else if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            startZoom(zoomValues[(zoomValues.indexOf(zoomValues.minBy { abs(scale - it) })-1).coerceIn(0..zoomValues.lastIndex)])
        }

        if(statusPre == IEntity.Feedback.CONTINUE) {
            statusPre = entitiesPre.update(this)
            return
        }

        if(isZooming) {

            if(abs(scale - zoomTarget) < zoomSpeed*2) {
                scale = zoomTarget
                zoomTarget = 0.0
                isZooming = false
            }

            if(scale > zoomTarget) {
                scale -= zoomSpeed
            }

            if(scale < zoomTarget) {
                scale += zoomSpeed
            }

        }

        /*
        if(RpgUtt.isKeyDown(GLFW.GLFW_KEY_UP)) {
            rotateX+=1f
        }
        if(RpgUtt.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            rotateX-=1f
        }
        */

        processTalking()

        entities.update(this)

    }

    override fun render(renderer: Renderer) {

        if(!isLoadingFinished) {

            GL11.glColor3f(0f, 0f, 0f)
            renderer.drawScreen()
            GL11.glColor3f(1f, 1f, 1f)
            renderer.renderStringCentered("Now Loading...", renderer.fontEn, Vec2d(0.0, 0.0), 0.2)
            return

        }

        if(statusPre == IEntity.Feedback.CONTINUE) {
            entitiesPre.render(this, renderer)
            return
        }

        val s = getScroll()

        renderer.matrix {

            GL11.glTranslated(s.x, s.y*RpgUtt.windowSize.x.toDouble()/RpgUtt.windowSize.y, 0.0)
            GL11.glScaled(1.0, RpgUtt.windowSize.x.toDouble()/RpgUtt.windowSize.y, 1.0)
            renderer.rotate(rotation)

            if(parameters[RENDER_TILES] != 0u) {

                val p = player.pos.pixel

                val numX = ceil(1.0/scale / 3 * 4).toInt()

                val numY = ceil(numX.toDouble() / 4 * 3).toInt()

                for(y in p.y - numY .. p.y + numY) for(x in p.x - numX .. p.x + numX) {

                    if(x < 0 || y < 0 || x >= map.size.x || y >= map.size.y) {

                        val tex = tileSet[parameters[TILE_ID_VOID]?.toInt() ?: 0]
                        val pos = getActualPos(Vec2i(x, y))
                        GL11.glColor4f(1f, 1f, 1f, 1f)
                        renderer.renderTexture(tex, pos, getActualSize(Vec2f.ONE), initColor=Vec4f.WHITE)

                        continue
                    }

                    val tex = tileSet[map[x, y].toShort().resizeToInt()]
                    val pos = GamePos(x, y)

                    GL11.glColor4f(1f, 1f, 1f, 1f)
                    renderer.renderTexture(tex, getActualPos(pos), getActualSize(Vec2f.ONE), initColor=Vec4f.WHITE)

                }

            }

            if(parameters[RENDER_ENTITIES] != 0u) {
                entities.render(this, renderer)
            }

        }

        currentSerifEntity?.render(this, renderer)

        if(DEBUG_MODE) {//TODO

            GL11.glColor3f(0f, 1f, 0.2f)

            renderer.renderTextLines(0.05,
                "world: ${tileSet.width}x${tileSet.width}",
                "player: ${player.pos}",
                String.format("scale: %.2f", 1.0 / scale),
                "entity: ${entities.childs.size}"
            )
        }

    }

    fun Renderer.renderTextLines(height: Double, vararg lines: String) {
        var offset = height
        lines.forEach {
            renderString(it, fontEn, vec(-0.95, 1.0 - offset), height)
            offset += height
        }
    }

    override fun reset() {

    }

    override fun onClicked(button: MouseButton, action: MouseAction): Boolean {

        return false
    }

    /**
     * **attention** It doesn't take Scroll Value in consideration. Scroll Value would be set by [GL11.glTranslated]
     */
    fun getActualPos(pos: GamePos): Vec2d {

        return pos.toVec2d()*scale - Vec2d.ONE

    }

    /**
     * **attention** It doesn't take scroll value in consideration. Scroll value would be set by [GL11.glTranslated]
     *
     * this function is for using signed values.
     */
    fun getActualPos(pos: Vec2i): Vec2d {

        return Vec2d(
            pos.x.toDouble(),
            pos.y.toDouble()
        ) * scale - Vec2d.ONE

    }

    fun getScroll(): Vec2d {

        val player = entities.childs.filterIsInstance<EntityPlayer>().first()

        return -getActualPos(player.pos)

    }

    fun getActualSize(size: Vec2f): Vec2d {

        return size.toVec2d()*scale

    }

    fun canEntityGoto(position: GamePos, d: Direction): Boolean {

        when(d) {
            Direction.NORTH, Direction.SOUTH -> {

                if(position.subpixel.y != 0)
                    return true

            }
            Direction.EAST, Direction.WEST -> {

                if(position.subpixel.x != 0)
                    return true

            }
        }

        val p0 = when(d) {
            Direction.NORTH, Direction.EAST -> position.pixel + d.component
            else -> position.pixel.toVec2i() + d.component
        }

        if(p0.x < 0 || p0.y < 0 || p0.x >= map.size.x || p0.y >= map.size.y || map[p0.x, p0.y] in barrierTiles)
            return false

        val p1 = p0 + when {
            (d == Direction.NORTH || d == Direction.SOUTH) && position.subpixel.x != 0 -> Direction.EAST.component
            (d == Direction.WEST || d == Direction.EAST) && position.subpixel.y != 0 -> Direction.NORTH.component
            else -> return true
        }

        if(map[p1.x, p1.y] in barrierTiles)
            return false

        return true

    }

    fun canPlayerGoto(d: Direction) = canEntityGoto(player.pos, d)

    fun talk(serif: Serif): Boolean {

        if(isTalking)
            return false

        isTalking = true

        currentSerifEntity = EntitySerif(serif)

        return true

    }

    fun processTalking() {

        val serifEntity = currentSerifEntity ?: return

        if(serifEntity.update(this) == IEntity.Feedback.FINISH) {

            currentSerifEntity = null
            isTalking = false

        }

    }

    enum class Parameter {
        RENDER_TILES,
        RENDER_ENTITIES,
        TILE_ID_VOID
        ;

        companion object {
            const val TRUE = 1u
            const val FALSE = 0u
        }

    }

}