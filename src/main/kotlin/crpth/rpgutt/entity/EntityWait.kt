package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.Vec4b
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

class EntityWait(val duration: Float) : EntityScript() {

    companion object {

        fun decode(stream: DataInputStream): EntityWait {
            return EntityWait(Float.fromBits(Vec4b(stream.readByte(), stream.readByte(), stream.readByte(), stream.readByte()).data.toInt()))
        }

    }

    var d = 0.0f

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        d += 1f / 60
        return if(d > duration) IEntity.Feedback.FINISH else IEntity.Feedback.CONTINUE

    }

    override fun encode(stream: DataOutputStream) {
        stream.writeFloat(duration)
    }

    override fun computeEncodedBinarySize() = 4

}