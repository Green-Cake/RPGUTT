package crpth.rpgutt

import crpth.rpgutt.scene.IScene
import crpth.rpgutt.scene.SceneTitle
import crpth.rpgutt.scene.SceneVoid
import crpth.util.Window
import crpth.util.logging.Logger
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.sound.SoundManager
import crpth.util.vec.Vec2f
import crpth.util.vec.Vec2i
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

object RpgUtt {

    val logger = Logger(this::class).apply {
        outputLevel = Logger.Level.FINER
    }

    val windowSize = Vec2i(1280, 960)

    var window = Window(NULL)
        private set

    val renderer = Renderer()

    val soundManager = SoundManager()

    var scene: IScene = SceneVoid

    var cursorPos = Vec2f.ZERO

    fun changeScene(another: IScene) {

        scene.reset()

        keyMapPrev.clear()
        keyMap.clear()

        scene = another

        another.init()

    }

    fun run() {

        logger.info("Thread: ${Thread.currentThread().name}")

        try {
            logger.info("Program [RPG-UTT] started")

            logger.config("Initialization started")
            init()
            logger.config("Main Loop started")
            loop()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            finish()
        }

    }

    private fun finish() {

        logger.config("the Game finished")

        soundManager.finish()

        window.freeCallbacks()
        window.destroy()

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()

        logger.info("Program [RPG-UTT] finished")

    }

    private fun init() {

        logger.info("LWJGL Version: ${Version.getVersion()}")

        soundManager.init()

        GLFWErrorCallback.createPrint(System.err).set()

        if(!glfwInit()) {
            throw IllegalStateException("Failed to initialize GLFW...")
        }

        val monitor = glfwGetPrimaryMonitor()
        val mode = glfwGetVideoMode(monitor)

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, TRUE)
        glfwWindowHint(GLFW_RESIZABLE, TRUE)

        window = Window.create(windowSize.x, windowSize.y, "RPG UTT")
        if(window.isNull()) {
            throw RuntimeException("Failed to create GLFW window...")
        }

        glfwSetKeyCallback(window.id, ::onKeyEvent)

        glfwSetCursorPosCallback(window.id) { w, x, y ->
            cursorPos = Vec2f(x.toFloat()/windowSize.x, 1.0f - y.toFloat()/windowSize.y)*2.0f - Vec2f.ONE
        }

        glfwSetMouseButtonCallback(window.id) { w, button, action, mods, ->
            scene.onClicked(MouseButton.from(button), MouseAction.from(action))
        }

        window.makeContextCurrent()
        glfwSwapInterval(1)

        changeScene(SceneTitle())

        val cx = DoubleArray(1)
        val cy = DoubleArray(1)
        glfwGetCursorPos(window.id, cx, cy)
        cursorPos = Vec2f(cx[0].toFloat()/windowSize.x, 1.0f - cy[0].toFloat()/windowSize.y)*2f - Vec2f.ONE

        window.show()

    }

    val keyMapPrev = mutableSetOf<Int>()
    val keyMap = mutableSetOf<Int>()

    fun onKeyEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {

        if(action == GLFW_RELEASE)
            keyMap.remove(key)
        else
            keyMap += key

    }

    private fun loop() = try {

        GL.createCapabilities()

        glEnable(GL_TEXTURE_2D)
        glEnable(GL_CULL_FACE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glCullFace(GL_BACK)

        while(!window.shouldClose()) { //loop start

            update()
            render()

            window.swapBuffers()
            glfwPollEvents()
        }

    } finally {

    }

    private fun update() {

        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        scene.update()

        keyMapPrev.clear()
        keyMapPrev.addAll(keyMap)

    }

    private fun render() {

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        scene.render(renderer)

    }

    fun isKeyPressed(key: Int): Boolean {
        return key !in keyMapPrev && key in keyMap
    }

    fun isKeyRepeated(key: Int): Boolean {
        return key in keyMapPrev && key in keyMap
    }

    fun isKeyDown(key: Int): Boolean {
        return key in keyMap
    }

    fun isKeyGetReleased(key: Int): Boolean {
        return key in keyMapPrev && key !in keyMap
    }

    fun isKeyReleased(key: Int): Boolean {
        return key !in keyMap
    }

}