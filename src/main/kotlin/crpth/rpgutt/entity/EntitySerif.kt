package crpth.rpgutt.entity

import crpth.rpgutt.RpgUtt
import crpth.rpgutt.scene.SceneMain
import crpth.rpgutt.script.lib.Serif
import crpth.util.render.Renderer
import crpth.util.vec.Vec2d
import crpth.util.vec.Vec2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.glColor4f
import java.io.DataOutputStream
import kotlin.math.roundToInt

class EntitySerif(val serif: Serif, var cursor: Int=0, var duration: Int=0, override val posZ: Int=-10) : EntityWithRendering(), Unencodable {

    var shouldFinish = false

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(shouldFinish || serif.atoms.isEmpty())
            return IEntity.Feedback.FINISH

        val atom = serif.atoms[cursor]

        ++duration

        if(atom.duration < duration) {

            if(serif.doAutoPaging) {

                duration = 0
                ++cursor

                if (cursor >= serif.atoms.size)
                    return IEntity.Feedback.FINISH

            } else {

                duration = atom.duration

            }

        }

        if(RpgUtt.richWindow.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {

            if(duration == atom.duration) {

                duration = 0
                ++cursor

                if (cursor >= serif.atoms.size) {
                    shouldFinish = true
                }

            } else {

                duration = atom.duration

            }

        }

        return IEntity.Feedback.CONTINUE

    }

    val fontHeight = 0.1

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        if(shouldFinish)
            return

        glColor4f(0.0f, 0.0f, 0.0f, 0.2f)
        renderer.drawSquare(Vec2f(-1f, -1f), Vec2f(2f, 2f))

        val atom = serif.atoms[cursor]
        var length = ((atom.textLines.sumOf { it.length } - 1)*(duration.toFloat()/atom.duration.toFloat())).roundToInt()

        glColor4f(0.2f, 0.2f, 0.2f, 1f)
        renderer.drawSquare(Vec2f(-0.9f, -0.9f), Vec2f(1.8f, 0.38f))

        for(n in 0 until atom.textLines.size) {

            if(length <= 0)
                break

            val line = atom.textLines[n]
            val text = if(line.length <= length) {
                length -= line.length
                line
            } else {
                val r = line.substring(0..length)
                length = 0
                r
            }

            renderer.color4b(atom.color)
            renderer.renderStringCentered(text, renderer.fontJa, Vec2d(0.0, -0.6 - fontHeight * n), 0.1)

        }

    }

    //undefined
    override fun encode(stream: DataOutputStream) {}

    override fun computeEncodedBinarySize() = 0

}