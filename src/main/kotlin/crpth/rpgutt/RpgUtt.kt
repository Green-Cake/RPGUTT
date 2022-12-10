package crpth.rpgutt

import crpth.mapbuilder.scene.SceneMapBuilder
import crpth.rpgutt.scene.IScene
import crpth.rpgutt.scene.SceneTitle
import crpth.rpgutt.scene.SceneVoid
import crpth.util.RichWindow
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
        private set

    val richWindow: RichWindow by lazy {
        RichWindow(window) { button, action ->
            scene.onClicked(richWindow, button, action)
        }
    }

    fun changeScene(another: IScene) {

        scene.reset()

        richWindow.resetInput()

        scene = another

        another.init()

    }

    fun run() {

        logger.info("Thread: ${Thread.currentThread().name}")

        try {
            logger.info("Program [RPG-UTT] started")

            logger.config("Initialization started")
            init(SceneTitle())
            logger.config("Main Loop started")
            loop()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            finish()
        }

    }

    fun runMapBuilder() {

        logger.info("Thread: ${Thread.currentThread().name}")
        logger.info("This is map builder!")

        try {
            logger.config("Initialization started")
            init(SceneMapBuilder)
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

    private fun init(scene: IScene?) {

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

        richWindow.init()

        window.makeContextCurrent()
        glfwSwapInterval(1)

        GL.createCapabilities()

        glEnable(GL_TEXTURE_2D)
        glEnable(GL_CULL_FACE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glCullFace(GL_BACK)

        if(scene != null)
            changeScene(scene)

        window.show()

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

        window.makeContextCurrent()
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        scene.update()

        richWindow.update()

    }

    private fun render() {

        window.makeContextCurrent()

        glClearColor(0f, 0f, 0f, 0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        scene.render(renderer)

    }

}