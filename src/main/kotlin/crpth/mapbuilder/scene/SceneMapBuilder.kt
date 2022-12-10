package crpth.mapbuilder.scene

import crpth.rpgutt.DEBUG_MODE
import crpth.rpgutt.FALSE
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.TRUE
import crpth.rpgutt.entity.*
import crpth.rpgutt.map.TileMap
import crpth.rpgutt.map.TileMapLoader
import crpth.rpgutt.scene.IScene
import crpth.rpgutt.scene.MapParameter
import crpth.util.RichWindow
import crpth.util.Window
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.vec.vec
import crpth.util.vec.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.ceil

object SceneMapBuilder : IScene {

    private var rotation = Vec3f(0.0f, 0.0f, 0.0f)

    private val tileSet by TileSet.createLazyInit("assets/rpgutt/textures/tile/BrightForest-A2.png", Vec2i(32, 32))

    private val barrierTiles = setOf<UShort>(
//        0u, 1u, 2u
    )

    private lateinit var map: TileMap

    private lateinit var entitiesPre: EntityParallel

    private lateinit var entities: EntityParallel

    private val zoomValues = listOf(0.05, 0.08, 0.1, 0.12)

    private var scale = zoomValues[1]

    private var scroll = GamePos.tile(0, 0)

    private val parameters = MapParameter.values().associateWithTo(EnumMap(MapParameter::class.java)) {

        when(it) {
            MapParameter.RENDER_TILES -> MapParameter.TRUE
            MapParameter.RENDER_ENTITIES -> MapParameter.TRUE
            MapParameter.TILE_ID_VOID -> 0u
        }

    }

    private var statusPre = IEntity.Feedback.CONTINUE

    private var isZooming = false
    private var zoomTarget = 0.0
    private var zoomSpeed = 0.002

    private var tileIDToPlace: UShort = 1u
    private var targetLayer: UInt = 0u

    private var windowPallet: Window = Window.NULL_W
    private val richWindowPallet by lazy {
        RichWindow(windowPallet, ::palletOnClicked)
    }

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

        RpgUtt.logger.info("SceneMapBuilder init start")

        windowPallet = Window.create(400, 800, "Pallet", share = RpgUtt.window)

        windowPallet.makeContextCurrent()

        GLFW.glfwSwapInterval(1)
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glCullFace(GL11.GL_BACK)
        GLFW.glfwSetWindowAttrib(windowPallet.id, GLFW.GLFW_RESIZABLE, FALSE)

        RpgUtt.window.makeContextCurrent()

        windowPallet.show()

        richWindowPallet.init()

        val t = thread {
            RpgUtt.logger.info("Map loading start")
            loadNewMap("generated_1")
            RpgUtt.logger.info("Map loading finished")
            isLoadingFinished = true
        }

//        loadSound("fantasy08", "bgm/fantasy08.ogg", true)
//        setVolume("fantasy08", 0.03)
//        play("fantasy08")

        GL11.glViewport(0, 0, RpgUtt.windowSize.x, RpgUtt.windowSize.y)

    }

    fun startZoom(value: Double) {
        isZooming = true
        zoomTarget = value
    }

    var tileSelectCoolTime = 0

    override fun update() {

        if(!isLoadingFinished)
            return

        richWindowPallet.update()

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            startZoom(zoomValues[(zoomValues.indexOf(zoomValues.minBy { abs(scale - it) })+1).coerceIn(0..zoomValues.lastIndex)])
        } else if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            startZoom(zoomValues[(zoomValues.indexOf(zoomValues.minBy { abs(scale - it) })-1).coerceIn(0..zoomValues.lastIndex)])
        }

        if(isZooming) {

            if(abs(scale - zoomTarget) < zoomSpeed *2) {
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

        val speed = if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) 256 else 128

        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_W)) {
            scroll += GamePos.sub(0, speed)
        }
        else if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_S)) {
            scroll -= GamePos.sub(0, speed)
        }
        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_D)) {
            scroll += GamePos.sub(speed, 0)
        }
        else if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_A)) {
            scroll -= GamePos.sub(speed, 0)
        }

        scroll = GamePos(scroll.value.coerceIn(0..(map.size.x*GamePos.PIXEL_AREA)))

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_I)) {
            targetLayer++
        } else if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_K)) {
            targetLayer--
        }

        if(tileSelectCoolTime != 0)
            --tileSelectCoolTime

        if(tileSelectCoolTime == 0 || RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_L)) {
                ++tileIDToPlace

                if(tileIDToPlace > tileSet.length.toUInt())
                    tileIDToPlace = 0u

                tileSelectCoolTime = 20
            } else if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_J)) {
                --tileIDToPlace

                if(tileIDToPlace > tileSet.length.toUInt())
                    tileIDToPlace = tileSet.length.toUShort()

                tileSelectCoolTime = 20
            }
        }
    }

    var RENDER_TILES = true

    override fun render(renderer: Renderer) {

        RpgUtt.renderer.fontManager.init()

        if(!isLoadingFinished) {

            GL11.glColor3f(0f, 0f, 0f)
            renderer.clearScreen()
            GL11.glColor3f(1f, 1f, 1f)
            renderer.renderStringCentered("Now Loading...", renderer.fontEn, Vec2d(0.0, 0.0), 0.2)
            return

        }

        renderPallet(renderer)

////        if(statusPre == IEntity.Feedback.CONTINUE) {
//////            entitiesPre.render(this, renderer)
////            return
////        }
//
//        GL11.glColor3f(1f, 1f, 1f)
//        renderer.renderString("hello?", renderer.fontEn, vec(10.0, 10.0), 0.08)

        val s = -getActualPos(scroll)

        renderer.matrix {

            val aspectRatio = RpgUtt.windowSize.x.toDouble()/RpgUtt.windowSize.y

            GL11.glTranslated(s.x, s.y*aspectRatio, 0.0)
            GL11.glScaled(1.0, aspectRatio, 1.0)//TODO

            renderer.rotate(rotation)

            if(RENDER_TILES) {

                val p = scroll.posInTiles

                val numX = ceil(1.0/ scale / 3 * 4).toInt()

                val numY = ceil(numX.toDouble() / 4 * 3).toInt()

                for(y in p.y - numY .. p.y + numY) for(x in p.x - numX .. p.x + numX) {

                    //render default tiles if (x, y) is out of range.
                    if(x < 0 || y < 0 || x >= map.size.x || y >= map.size.y) {

                        val pos = getActualPos(Vec2i(x, y))
                        val tex = tileSet[0]
                        renderer.renderTexture(tex, pos, getActualSize(Vec2f.ONE), initColor = Vec4f(0.5f, 0.5f, 0.5f))

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
                            getActualSize(Vec2f.ONE)
                        )
                    }

                }

            }

//            entities.render(this, renderer)

        }

//        currentSerifEntity?.render(this, renderer)

        if(DEBUG_MODE) {//TODO

            GL11.glColor3f(0f, 1f, 0.2f)

            renderer.renderTextLines(0.05,
                "layer: $targetLayer",
                "tile: $tileIDToPlace",
            )
        }

    }

    private fun renderPallet(renderer: Renderer) {

        windowPallet.makeContextCurrent()

        GL11.glMatrixMode(GL11.GL_MODELVIEW)
        GL11.glLoadIdentity()

        GL11.glClearColor(0f, 0f, 0f, 0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        val tileSize = vec(0.2, 0.2)

        renderer.matrix {

            val windowSize = windowPallet.getWindowSize()

            val aspectRatio = windowSize.x.toDouble() / windowSize.y
            GL11.glScaled(1.0, aspectRatio, 1.0)

            for(i in 0 .. tileSet.length) {

                val x = i % 10
                val y = i / 10

                if(i == 0)
                    continue

                val tex = tileSet[i-1]
                renderer.renderTexture(tex, vec(-1.0, 1.0/aspectRatio) + tileSize * vec(x, -y-1), tileSize)

            }

        }

        windowPallet.swapBuffers()

        RpgUtt.window.makeContextCurrent()

    }

    private fun palletOnClicked(button: MouseButton, action: MouseAction): Boolean {

        if(button == MouseButton.LEFT) {
            val windowSize = windowPallet.getWindowSize()

            val aspectRatio = windowSize.x.toDouble()/windowSize.y

            val cpos = richWindowPallet.cursorPos

            val x = (10 * (1+cpos.x)/2).toInt()
            val y = (10/aspectRatio * (1-cpos.y)/2).toInt()

            tileIDToPlace = (y*10 + x).coerceIn(0 .. tileSet.length).toUShort()
        }

        return true
    }

    fun Renderer.renderTextLines(height: Double, vararg lines: String) {
        var offset = height
        lines.forEach {
            renderString(it, fontEn, vec(-0.95, 1.0 - offset), height)
            offset += height
        }
    }

    override fun reset() {
        richWindowPallet.resetInput()
    }

    override fun onClicked(window: RichWindow, button: MouseButton, action: MouseAction): Boolean {

        val cp = window.cursorPos.toVec2d()

        val tmp = 1.0 / scale

        val t = Vec2d(tmp, tmp/RpgUtt.windowSize.x.toDouble()*RpgUtt.windowSize.y)

        val clickedPos = GamePos.fromVec2d(cp * t) + scroll

        if(button == MouseButton.LEFT && action == MouseAction.RELEASE) {

            if(clickedPos.x >= 0 && clickedPos.y >= 0 && targetLayer.toInt() in 0 until map.layerCount) {
                map[targetLayer.toInt(), clickedPos.posInTiles.x, clickedPos.posInTiles.y] = tileIDToPlace
            }
            return true
        }

        if(button == MouseButton.MIDDLE && action == MouseAction.RELEASE) {

            if(clickedPos.x >= 0 && clickedPos.y >= 0 && targetLayer.toInt() in 0 until map.layerCount) {

                tileIDToPlace = map[targetLayer.toInt(), clickedPos.posInTiles.x, clickedPos.posInTiles.y]

            }

        }

        return false
    }

    /**
     * **attention** It doesn't take Scroll Value in consideration. Scroll Value would be set by [GL11.glTranslated]
     */
    fun getActualPos(pos: GamePos): Vec2d {

        return pos.toVec2d()* scale - Vec2d.ONE

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

    fun getActualSize(size: Vec2f): Vec2d {

        return size.toVec2d()* scale

    }

}