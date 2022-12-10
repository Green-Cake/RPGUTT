package crpth.rpgutt.scene

import crpth.rpgutt.DEBUG_MODE
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.*
import crpth.rpgutt.map.TileMap
import crpth.rpgutt.map.TileMapLoader
import crpth.rpgutt.scene.MapParameter.*
import crpth.rpgutt.script.lib.Serif
import crpth.util.RichWindow
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.type.BoundingBox
import crpth.util.vec.vec
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.ceil

object SceneMain : IScene {

    private var rotation = Vec3f(0.0f, 0.0f, 0.0f)

    private val tileSet by TileSet.createLazyInit("assets/rpgutt/textures/tile/BrightForest-A2.png", Vec2i(32, 32))

    private val barrierTiles = setOf<UShort>(
//        0u, 1u, 2u
    )

    private lateinit var map: TileMap

    private lateinit var entitiesPre: EntityParallel

    internal lateinit var entities: EntityParallel

    private val zoomValues = listOf(0.05, 0.08, 0.1, 0.12)

    private var scale = zoomValues[1]

    val renderingBound: BoundingBox get() {

        val numX = 1.0/scale / 3 * 4
        val numY = numX / 4 * 3

        return BoundingBox.fromPosAndSize(getScroll(), Vec2d(numX, numY))

    }

    private val parameters = values().associateWithTo(EnumMap(MapParameter::class.java)) {

        when(it) {
            RENDER_TILES -> MapParameter.TRUE
            RENDER_ENTITIES -> MapParameter.TRUE
            TILE_ID_VOID -> 0u
        }

    }

    private var statusPre = IEntity.Feedback.CONTINUE

    var isTalking = false

    private var currentSerifEntity: EntitySerif? = null

    val player: EntityPlayer get() = entities.childs.filterIsInstance<EntityPlayer>().first()

    val playerCanMove get() = !isTalking

    private var isZooming = false
    private var zoomTarget = 0.0
    private var zoomSpeed = 0.002

    fun setParam(p: MapParameter, value: UInt) {
        parameters[p] = value
    }

    fun getParam(p: MapParameter) = parameters.getOrDefault(p, 0u)

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

        loadSound("fantasy08", "bgm/fantasy08.ogg", true)
        setVolume("fantasy08", 0.03)
        play("fantasy08")

        GL11.glViewport(0, 0, RpgUtt.windowSize.x, RpgUtt.windowSize.y)

    }

    fun startZoom(value: Double) {
        isZooming = true
        zoomTarget = value
    }

    override fun update() {

        if(!isLoadingFinished)
            return

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            startZoom(zoomValues[(zoomValues.indexOf(zoomValues.minBy { abs(scale - it) })+1).coerceIn(0..zoomValues.lastIndex)])
        } else if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
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

        if(DEBUG_MODE) {

            if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_L)) {

                entities.requestAddEntity(EntityMovable("rock", Vec2i(16, 16), player.pos.plus(GamePos.tile(2, 2)), Vec2f.ONE))
//                entities.requestAddEntity(EntityGimmick("rock", Vec2i(16, 16), player.pos.plus(GamePos(2, 2)), Vec2f(1f, 1f)))

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
            renderer.clearScreen()
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

                val p = player.pos.posInTiles

                val numX = ceil(1.0/scale / 3 * 4).toInt()

                val numY = ceil(numX.toDouble() / 4 * 3).toInt()

                for(y in p.y - numY .. p.y + numY) for(x in p.x - numX .. p.x + numX) {

                    if(x < 0 || y < 0 || x >= map.size.x || y >= map.size.y) {

                        val tileIDVoid = parameters[TILE_ID_VOID]?.toInt() ?: 0

                        val pos = getActualPos(Vec2i(x, y))
                        val tex = if(tileIDVoid == 0) null else tileSet[tileIDVoid-1]
                        GL11.glColor4f(1f, 1f, 1f, 1f)
                        if(tex != null)
                            renderer.renderTexture(tex, pos, getActualSize(Vec2f.ONE))

                        continue
                    }

                    val pos = GamePos.tile(x, y)

                    for (layer in 0 until map.layerCount){

                        val tileID = map[layer, x, y].toShort().resizeToInt()

                        if(tileID == 0)
                            continue

                        val tex = tileSet[tileID-1]
                        GL11.glColor4f(1f, 1f, 1f, 1f)
                        renderer.renderTexture(
                            tex,
                            getActualPos(pos),
                            getActualSize(Vec2f.ONE),
                            initColor = Vec4f.WHITE
                        )
                    }

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

    override fun onClicked(window: RichWindow, button: MouseButton, action: MouseAction): Boolean {

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

    fun canEntityGoto(entity: EntityObject, d: Direction): Boolean {

        val position = entity.pos

        if(entities.childs.filterIsInstance<EntityObject>().count {
                it != entity && it.intersects(entity.pos.toVec2f() + d.component.toVec2f() / GamePos.BITS_FOR_SUBPIXEL.toFloat(), entity.size)
            } != 0)
            return false

        when(d) {
            Direction.NORTH, Direction.SOUTH -> {

                if(position.subpos.y != 0)
                    return true

            }
            Direction.EAST, Direction.WEST -> {

                if(position.subpos.x != 0)
                    return true

            }
        }

        val p0 = when(d) {
            Direction.NORTH, Direction.EAST -> position.posInTiles + d.component
            else -> position.posInTiles.toVec2i() + d.component
        }

        if(p0.x < 0 || p0.y < 0 || p0.x >= map.size.x || p0.y >= map.size.y || map[0, p0.x, p0.y] in barrierTiles)
            return false

        val p1 = p0 + when {
            (d == Direction.NORTH || d == Direction.SOUTH) && position.subpos.x != 0 -> Direction.EAST.component
            (d == Direction.WEST || d == Direction.EAST) && position.subpos.y != 0 -> Direction.NORTH.component
            else -> return true
        }

        if(map[0, p1.x, p1.y] in barrierTiles)
            return false

        return true

    }

    fun canPlayerGoto(d: Direction) = canEntityGoto(player, d)

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

}