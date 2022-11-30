package crpth.rpgutt.script

import crpth.rpgutt.ResourceManager
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.EntityPerson
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.IEntityAI
import java.io.File
import kotlin.script.experimental.host.StringScriptSource

object AIManager {

    val loadedAIs = mutableMapOf<UShort, IEntityAI>()

    fun get(entityID: UShort): IEntityAI? = loadedAIs[entityID]

    fun get(entity: EntityPerson): IEntityAI? = get(entity.id)

    @Throws(NoSuchFileException::class)
    fun load(entity: EntityPerson, scriptSrcPath: String): IEntityAI {

        if(entity.id in loadedAIs)
            return loadedAIs[entity.id]!!

        val script = try {
            if(scriptSrcPath == "null") {
                RpgUtt.logger.warn("Script file not specified! (entity: $entity.name)")
                throw NoSuchFileException(File(ClassLoader.getSystemResource("assets/rpgutt/script/$scriptSrcPath.kts").toURI()))
            } else {
                ScriptEvaluator.compile(StringScriptSource(ResourceManager.loadScriptSrc(scriptSrcPath)))
            }
        } catch (t: Throwable) {
            throw NoSuchFileException(File(ClassLoader.getSystemResource("assets/rpgutt/script/$scriptSrcPath.kts").toURI()))
        }

        val ai = ScriptEvaluator.eval<Any?>(EntityParams(entity, "update"), script) as IEntityAI

        loadedAIs[entity.id] = ai

        return ai

    }

}