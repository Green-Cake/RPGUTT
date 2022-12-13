package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.ISceneStage
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

interface IEntity {

    val id: UShort get() = EntityManager.getID(this::class) ?: UShort.MAX_VALUE

    fun update(sceneStage: ISceneStage): Feedback

    fun encode(stream: DataOutputStream)

    fun encode(): ByteArray {

        val bos = ByteArrayOutputStream()

        val stream = DataOutputStream(bos)

        encode(stream)

        stream.close()

        return bos.toByteArray()

    }

    fun createFactory() = EntityFactory(id, encode())

    enum class Feedback {
        CONTINUE,
        FINISH
    }

}