package crpth.rpgutt.entity

import crpth.rpgutt.scene.MapParameter
import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.Vec2b
import crpth.util.vec.readFrom
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityMapConfig(val param: MapParameter, val value: UInt) : EntityScript() {

    companion object {

        fun decode(stream: DataInputStream): EntityMapConfig {
            val param = MapParameter.values()[Vec2b.readFrom(stream).data.toShort().resizeToInt()]
            val value = stream.readInt().toUInt()
            return EntityMapConfig(param, value)
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