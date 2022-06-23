package crpth.rpgutt.scene

import crpth.rpgutt.RpgUtt
import crpth.util.gui.Gui
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Matrix
import crpth.util.render.Renderer
import crpth.util.render.Texture
import crpth.util.vec.Vec2d
import crpth.util.vec.Vec3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import java.awt.Font
import kotlin.random.Random

class SceneTitle : SceneParent() {

    init {

        addSubScene(TitleTop())
        addSubScene(TitleContinue())
        addSubScene(TitleConfig())
        addSubScene(TitleHowToPlay())
        addSubScene(TitleCredits())

        changeSub(0)

    }

    override fun reset() {

        stop("fantasy09")

        subScenes.values.forEach(SubScene::reset)

    }

    override fun init() {

        loadSound("fantasy09", "bgm/fantasy09.ogg", true)
        loadSound("enter1", "se/enter1.wav", false)
        loadSound("enter2", "se/enter2.wav", false)
        loadSound("enter3", "se/enter3.wav", false)

        setVolume("fantasy09", 0.1)

        play("fantasy09")

    }

    inner class TitleTop : SubScene(0, this) {

        var fadeToPlay: Double = Double.NaN

        val const0 = Random.nextInt(128) == 0

        val titleLogo by Texture.createLazyInit("logo.png")

        var select = 0

        override fun init() {

        }

        override fun reset() {
            select = 0
            fadeToPlay = Double.NaN
        }

        override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
            return false
        }

        override fun update() {

            println(System.nanoTime())

            if(!fadeToPlay.isNaN()) {

                fadeToPlay -= 1
                setVolume("fantasy09", fadeToPlay/900.0)

                if(fadeToPlay < 0) {

                    RpgUtt.changeScene(SceneMain)

                }

                return

            }

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                GLFW.glfwSetWindowShouldClose(RpgUtt.window.id, true)
            }

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_UP)) {
                --select
            }

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
                ++select
            }

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ENTER) || RpgUtt.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {

                play(if(select == 0) "enter1" else "enter3", volume = 0.2)

                when(select) {
                    0 -> {
                        fadeToPlay = 180.0
                    }
                    1 -> root.changeSub(1)
                    2 -> root.changeSub(2)
                    3 -> root.changeSub(3)
                    4 -> root.changeSub(4)
                }

            }

        }

        val matrix0 = Matrix(Vec3f(0.0f, 0.0f, -0.3f), Vec3f.ZERO)

        private inline fun s0(renderer: Renderer, i: Int, crossinline block: ()->Unit) {

            if(select == i) {
                renderer.doDrawUnderline = true
                GL11.glColor3d(0.2, 1.0, 0.2)

                renderer.matrix(matrix0, block)

            } else {
                renderer.doDrawUnderline = false
                GL11.glColor3d(1.0, 1.0, 1.0)
                block()
            }

        }

        var tick = -1

        override fun render(renderer: Renderer) {

            ++tick

            select = select.mod(5)

            if(const0)
                GL11.glColor3d(Random.nextDouble(), 0.0, Random.nextDouble())
            else
                GL11.glColor3d(0.375 + Random.nextDouble()/4.0, 0.375 + Random.nextDouble()/4.0, 0.375 + Random.nextDouble()/4.0)

            renderer.renderTexture(titleLogo, Vec2d(-0.9, 0.0), Vec2d(1.8, 0.9))

            s0(renderer, 0) {
                renderer.renderStringCentered("PLAY", renderer.fontManager.fontMonospaced, Vec2d(0.0, -0.25), 0.13)
            }
            s0(renderer, 1) {
                renderer.renderStringCentered("CONTINUE", renderer.fontManager.fontMonospaced, Vec2d(0.0, -0.40), 0.13)
            }
            s0(renderer, 2) {
                renderer.renderStringCentered("CONFIG", renderer.fontManager.fontMonospaced, Vec2d(0.0, -0.55), 0.13)
            }
            s0(renderer, 3) {
                renderer.renderStringCentered("HOW TO PLAY", renderer.fontManager.fontMonospaced, Vec2d(0.0, -0.70), 0.13)
            }
            s0(renderer, 4) {
                renderer.renderStringCentered("CREDITS", renderer.fontManager.fontMonospaced, Vec2d(0.0, -0.85), 0.13)
            }
            if(!fadeToPlay.isNaN()) {

                GL11.glColor4d(0.0, 0.0, 0.0, 1.0 - fadeToPlay/180.0)
                GL11.glBegin(GL11.GL_QUADS)
                GL11.glVertex2d(-1.0, 1.0)
                GL11.glVertex2d(-1.0, -1.0)
                GL11.glVertex2d(1.0, -1.0)
                GL11.glVertex2d(1.0, 1.0)
                GL11.glEnd()

            }

        }

    }

    inner class TitleContinue : SubScene(1, this) {

        var rotate = 45.0f

        var dataLoaded = false

        override fun init() {

        }

        override fun reset() {

        }

        override fun update() {

            if(!dataLoaded) {



                dataLoaded = true
            }

            if(rotate > 1f)
                rotate /= 1.05f
            else
                rotate = 0f

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                rotate = 45.0f
                root.changeSub(0, false)
            }

        }

        override fun render(renderer: Renderer) {

            GL11.glRotatef(rotate, 1f, 0f, 0f)

            renderer.doDrawUnderline = false

            renderer.renderStringCentered("CONTINUE", renderer.fontManager.fontMonospaced, Vec2d(0.0, 0.8), 0.1)

        }

        override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
            return false
        }

    }

    inner class TitleConfig : SubScene(2, this) {

        val gui = Gui()

        var rotate = 45.0f

        override fun init() {

        }

        override fun reset() {

        }

        override fun update() {

            if(rotate > 1f)
                rotate /= 1.05f
            else
                rotate = 0f

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                rotate = 45.0f
                root.changeSub(0, false)
            }

            gui.update()

        }

        override fun render(renderer: Renderer) {

            GL11.glRotatef(rotate, 1f, 0f, 0f)

            renderer.doDrawUnderline = false

            renderer.renderStringCentered("CONFIG", renderer.fontManager.fontMonospaced, Vec2d(0.0, 0.8), 0.1)

            gui.render(renderer)

        }

        override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
            return gui.onClicked(button, action)
        }

    }

    inner class TitleHowToPlay : SubScene(3, this) {

        var rotate = 45.0f

        override fun init() {

        }

        override fun reset() {

        }

        override fun update() {

            if(rotate > 1f)
                rotate /= 1.05f
            else
                rotate = 0f

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                rotate = 45.0f
                root.changeSub(0, false)
            }

        }

        private fun Renderer.renderTextLines(font: Font, pos: Vec2d, height: Double, marginBetweenLine: Double, texts: Iterable<String>) {

            texts.forEachIndexed { index, s ->

                renderStringCentered(s, fontManager.fontMonospaced, pos.minus(0.0, (height+marginBetweenLine)*index), height)

            }

        }

        private fun Renderer.renderTextLines(font: Font, pos: Vec2d, height: Double, marginBetweenLine: Double, vararg texts: String) =
            this.renderTextLines(font, pos, height, marginBetweenLine, texts.asIterable())

        private fun Renderer.renderTextLines(font: Font, pos: Vec2d, height: Double, marginBetweenLine: Double, textMultiLine: String) =
            this.renderTextLines(font, pos, height, marginBetweenLine, textMultiLine.lines())

        override fun render(renderer: Renderer) {

            GL11.glRotatef(rotate, 1f, 0f, 0f)

            renderer.doDrawUnderline = false

            renderer.renderStringCentered("HOW TO PLAY", renderer.fontManager.fontMonospaced, Vec2d(0.0, 0.8), 0.15)

            renderer.renderTextLines(renderer.fontManager.fontYumincho, Vec2d(0.0, 0.60), 0.08, 0.0,
                """
                    このげーむはねぇ、まだ肝心の内容が一切作られてないんだぁ、、、
                    タイトルしか作ってねぇんだ、、、
                    だからHow to playもなにもねぇんだよなぁ
                    うにゃあ
                """.trimIndent()
            )

        }

        override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
            return false
        }

    }

    inner class TitleCredits : SubScene(4, this) {

        var rotate = 45.0f

        override fun init() {

        }

        override fun reset() {

        }

        override fun update() {

            if(rotate > 1f)
                rotate /= 1.05f
            else
                rotate = 0f

            if(RpgUtt.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                rotate = 45.0f
                root.changeSub(0, false)
            }

        }

        override fun render(renderer: Renderer) {

            GL11.glRotatef(rotate, 1f, 0f, 0f)

            renderer.doDrawUnderline = false

            renderer.renderStringCentered("CREDITS", renderer.fontManager.fontMonospaced, Vec2d(0.0, 0.8), 0.15)

            renderer.renderStringCentered("CREATOR: Carpathia", renderer.fontManager.fontMonospaced, Vec2d(0.0, 0.6), 0.13)

            renderer.renderStringCentered("Music/SE: 魔王魂", null, Vec2d(0.0, 0.5), 0.13)

        }

        override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
            return false
        }

    }

}