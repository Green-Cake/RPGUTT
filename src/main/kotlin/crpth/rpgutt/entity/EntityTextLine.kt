package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f
import crpth.util.vec.Vec4b
import crpth.util.vec.readFrom
import crpth.util.vec.resizeToInt
import org.lwjgl.opengl.GL11
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityTextLine(val str: String, val pos: Vec2f, val height: Float, val color: Vec4b, val duration: Float, val fadeIn: Float, val fadeOut: Float) : EntityWithRendering() {

    companion object {

        fun decode(stream: DataInputStream): EntityTextLine {

            val numberOfBytes = stream.readInt()
            val bytes = ByteArray(numberOfBytes) { stream.read().toByte() }

            val str = String(bytes)
            val pos = Vec2f.readFrom(stream)
            val height = stream.readFloat()
            val color = Vec4b.readFrom(stream)
            val duration = stream.readFloat()
            val fadeIn = stream.readFloat()
            val fadeOut = stream.readFloat()

            return EntityTextLine(str, pos, height, color, duration, fadeIn, fadeOut)
        }

    }

    val timeSum = fadeIn + duration + fadeOut

    var d = 0.0f

    //str.length + 32
    override fun encode(stream: DataOutputStream) {
        val bytes = str.toByteArray()
        stream.writeInt(bytes.size)//4
        stream.write(bytes)//n
        stream.writeLong(pos.data.toLong())//8
        stream.writeFloat(height)//4
        stream.writeInt(color.data.toInt())//4
        stream.writeFloat(duration)//4
        stream.writeFloat(fadeIn)//4
        stream.writeFloat(fadeOut)//4
    }

    override fun computeEncodedBinarySize() = str.toByteArray().size + 32

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        d += 1.0f/60

        if(d > timeSum) {
            d = 0f
            return IEntity.Feedback.FINISH

        }

        return IEntity.Feedback.CONTINUE

    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        val r = color.r.resizeToInt()
        val g = color.g.resizeToInt()
        val b = color.b.resizeToInt()
        val a = color.a.resizeToInt()

        GL11.glColor4f(r/255f, g/255f, b/255f,  a/255f * when {
            d <= fadeIn -> d / fadeIn
            d < fadeOut -> 1f
            else -> 1f - (d - fadeIn - duration) / fadeOut
        })

        renderer.renderStringCentered(str, renderer.fontManager.fontMonospaced, pos.toVec2d(), height.toDouble())

    }

}