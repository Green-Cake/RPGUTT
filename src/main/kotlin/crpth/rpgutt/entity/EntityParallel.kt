package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityParallel(val entities: Array<out IEntity>) : EntityWithRendering() {

    companion object {

        fun decode(stream: DataInputStream): EntityParallel {

            val number = stream.readShort().resizeToInt()

            val entities = Array(number) {

                val c_id = stream.readShort().toUShort()
                val size = stream.readShort().resizeToInt()
                val c_meta = ByteArray(size) { stream.readByte() }
                val factory = EntityFactory(c_id, c_meta)
                EntityManager.createEntity(factory) ?: throw Exception()

            }

            return EntityParallel(entities)

        }

    }

    val status = Array(entities.size) { IEntity.Feedback.CONTINUE }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(entities.isEmpty())
            return IEntity.Feedback.FINISH

        entities.forEachIndexed { index, entity ->

            if(status[index] == IEntity.Feedback.FINISH)
                return@forEachIndexed

            status[index] = entity.update(sceneMain)

        }

        return if(IEntity.Feedback.CONTINUE !in status)
            IEntity.Feedback.FINISH
        else
            IEntity.Feedback.CONTINUE

    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        entities.forEachIndexed { index, entity ->

            if(status[index] == IEntity.Feedback.FINISH)
                return@forEachIndexed

            (entity as? EntityWithRendering)?.render(sceneMain, renderer)

        }

    }

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

}