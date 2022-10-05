package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityParallel(val entities: MutableList<out IEntity>) : EntityWithRendering() {

    companion object {

        fun decode(stream: DataInputStream): EntityParallel {

            val number = stream.readShort().resizeToInt()

            val entities = MutableList(number) {

                val c_id = stream.readShort().toUShort()
                val size = stream.readShort().resizeToInt()
                val c_meta = ByteArray(size) { stream.readByte() }
                val factory = EntityFactory(c_id, c_meta)
                EntityManager.createEntity(factory) ?: throw Exception()

            }

            return EntityParallel(entities)

        }

    }

    override fun update(sceneMain: SceneMain): IEntity.Feedback {

        if(entities.isEmpty())
            return IEntity.Feedback.FINISH

        val finishedEntities = mutableSetOf<IEntity>()

        entities.forEachIndexed { index, entity ->

            if(entity.update(sceneMain) == IEntity.Feedback.FINISH) {
                finishedEntities += entity
            }

        }

        entities.removeAll(finishedEntities)

        if(entities.isEmpty())
            return IEntity.Feedback.FINISH

        return IEntity.Feedback.CONTINUE

    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        entities.forEachIndexed { index, entity ->

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