package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.type.BoundingBox
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityParallel(private val _entities: MutableList<IEntity>) : EntityWithRendering() {

    val entities: List<IEntity> get() = _entities

    private val entries = mutableListOf<IEntity>()

    private val minusEntry = mutableListOf<IEntity>()

    fun requestAddEntity(entity: IEntity) {
        entries += entity
    }

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

        if(_entities.isEmpty())
            return IEntity.Feedback.FINISH

        val status = Array(_entities.size) { IEntity.Feedback.CONTINUE }

        _entities.forEachIndexed { index, entity ->

            if(status[index] == IEntity.Feedback.FINISH) {
                minusEntry += entity
                return@forEachIndexed
            }
            status[index] = entity.update(sceneMain)

        }

        _entities.addAll(entries)
        entries.clear()
        _entities.removeAll(minusEntry)
        minusEntry.clear()

        return if(IEntity.Feedback.CONTINUE !in status)
            IEntity.Feedback.FINISH
        else
            IEntity.Feedback.CONTINUE

    }

    override fun render(sceneMain: SceneMain, renderer: Renderer) {

        _entities.forEachIndexed { index, entity ->

            (entity as? EntityWithRendering)?.render(sceneMain, renderer)

        }

    }

    override fun doRender(sceneMain: SceneMain, renderer: Renderer, bound: BoundingBox) = _entities.isNotEmpty()

    override fun encode(stream: DataOutputStream) {

        stream.writeShort(_entities.size)

        _entities.forEach {

            val encoded = it.encode()
            stream.writeShort(it.id.toInt())
            stream.writeShort(encoded.size)
            stream.write(encoded)

        }

    }

    override fun computeEncodedBinarySize() = _entities.sumOf { it.computeEncodedBinarySize() + 4 } + 2

}