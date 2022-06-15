package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import crpth.rpgutt.scene.SceneMain
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

interface IEntity {

    val id: UShort get() = EntityManager.getID(this::class) ?: UShort.MAX_VALUE

    fun update(sceneMain: SceneMain): Feedback

    fun encode(stream: DataOutputStream)

    @Deprecated("Unused!", ReplaceWith("0"))
    fun computeEncodedBinarySize(): Int = 0

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