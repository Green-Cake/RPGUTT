package crpth.rpgutt.entity

import crpth.rpgutt.map.EntityFactory
import java.io.DataInputStream
import kotlin.reflect.KClass

object EntityManager {

    private val entities = mutableMapOf<UShort, KClass<out IEntity>>()

    private val entityDecoders = mutableMapOf<UShort, (DataInputStream)->IEntity>()

    fun getID(clazz: KClass<out IEntity>) = entities.keys.firstOrNull { entities[it] == clazz }

    fun createEntity(factory: EntityFactory) = entityDecoders[factory.id]?.invoke(DataInputStream(factory.meta.inputStream()))

    fun <T : IEntity> register(id: UShort, clazz: KClass<T>, decoder: (DataInputStream)->T): Boolean {

        if(id in entities || clazz in entities.values)
            return false

        entities[id] = clazz
        entityDecoders[id] = decoder

        return true
    }

    init {

        register(0u, EntityOrderedList::class, EntityOrderedList::decode)
        register(1u, EntityParallel::class, EntityParallel::decode)
        register(10u, EntityWait::class, EntityWait::decode)
        register(11u, EntitySceneConfig::class, EntitySceneConfig::decode)

        register(20u, EntityTextLine::class, EntityTextLine::decode)

        register(30u, EntityPerson::class, EntityPerson::decode)
        register(100u, EntityPlayer::class, EntityPlayer::decode)

    }

}