package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityOrderedList(val entities: Array<out IEntity>) : EntityWithRendering() {
    companion object {

        fun decode(stream: DataInputStream): EntityOrderedList {

            val number = stream.readShort().resizeToInt()

            val entities = Array(number) {

                val c_id = stream.readShort().toUShort()
                val size = stream.readShort().resizeToInt()
                val c_meta = ByteArray(size) { stream.readByte() }
                val factory = EntityFactory(c_id, c_meta)
                EntityManager.createEntity(factory) ?: throw Exception()

            }

            return EntityOrderedList(entities)

        }

    }

    var cursor = 0

    override fun encode(stream: DataOutputStream) {

        stream.writeShort(entities.size)

        entities.forEach {

            val encoded = it.encode()
            stream.writeShort(it.id.toInt())
            stream.writeShort(encoded.size)
            stream.write(encoded)

        }

    }

    override fun computeEncodedBinarySize() = entities.sumOf { it.computeEncodedBinarySize() + 4 } + 2

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(entities.isEmpty())
            return IEntity.Feedback.FINISH

        if(cursor > entities.lastIndex)
            return IEntity.Feedback.FINISH

        val feedback = entities[cursor].update(sceneMain)

        if(feedback == IEntity.Feedback.FINISH) {

            ++cursor

            if(cursor > entities.lastIndex) {
                return IEntity.Feedback.FINISH
            }
        }

        return IEntity.Feedback.CONTINUE

    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        if(cursor > entities.lastIndex)
            return

        (entities[cursor] as? EntityWithRendering)?.render(sceneMain, renderer)

    }
}