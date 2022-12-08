package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.vec.*
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityGimmickPressureButton(pos: GamePos, size: Vec2f, val targetID: Int) : EntityGimmick("pressure_button", Vec2i(16, 16), pos, size) {

    companion object {

        fun decode(stream: DataInputStream) = EntityGimmickPressureButton(GamePos.readFrom(stream), Vec2f.readFrom(stream), stream.readInt())

    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(sceneMain.entities.childs.filterIsInstance<EntityMovable>().count { this.intersects(it) } > 0) {



        }

        return IEntity.Feedback.CONTINUE
    }

    override fun encode(stream: DataOutputStream) {
        pos.encode(stream)
        stream.writeLong(size.data.toLong())
        stream.writeInt(targetID)
    }

}