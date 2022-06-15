package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.Vec2b
import crpth.util.vec.Vec4b
import crpth.util.vec.readFrom
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

class EntitySceneConfig(val param: SceneMain.Parameter, val value: UInt) : EntityScript() {

    companion object {

        fun decode(stream: DataInputStream): EntitySceneConfig {
            val param = SceneMain.Parameter.values()[Vec2b.readFrom(stream).data.toShort().resizeToInt()]
            val value = stream.readInt().toUInt()
            return EntitySceneConfig(param, value)
        }

    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        SceneMain.setParam(param, value)

        return IEntity.Feedback.FINISH
    }

    override fun encode(stream: DataOutputStream) {
        stream.writeShort(param.ordinal)
        stream.writeInt(value.toInt())
    }

    override fun computeEncodedBinarySize() = 6

}