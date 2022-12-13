package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.ISceneStage
import crpth.util.render.Renderer
import crpth.util.type.BoundingBox
import crpth.util.vec.resizeToInt
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityParallel(entityList: List<IEntity>, override val posZ: Int=0) : EntityWithRendering() {

    internal val _entities: MutableList<IEntity> = entityList.toMutableList()

    val childs: List<IEntity> get() = _entities

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

    override fun update(sceneStage: ISceneStage): IEntity.Feedback {

        if(_entities.isEmpty())
            return IEntity.Feedback.FINISH

        val status = Array(_entities.size) { IEntity.Feedback.CONTINUE }

        _entities.forEachIndexed { index, entity ->

            if(status[index] == IEntity.Feedback.FINISH) {
                minusEntry += entity
                return@forEachIndexed
            }
            status[index] = entity.update(sceneStage)

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

    override fun render(sceneMain: ISceneStage, renderer: Renderer) {

        _entities.filterIsInstance<EntityWithRendering>().sortedBy { it.posZ }.forEachIndexed { index, entity ->

            entity.render(sceneMain, renderer)

        }

    }

    override fun isRenderingTarget(sceneMain: ISceneStage, renderer: Renderer, bound: BoundingBox) = _entities.isNotEmpty()

    override fun encode(stream: DataOutputStream) {

        stream.writeShort(_entities.size)

        _entities.forEach {

            val encoded = it.encode()
            stream.writeShort(it.id.toInt())
            stream.writeShort(encoded.size)
            stream.write(encoded)

        }

    }

}