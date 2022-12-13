package crpth.rpgutt.script

import crpth.rpgutt.ResourceManager
import crpth.rpgutt.RpgUtt
import crpth.rpgutt.entity.EntityPerson
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.IEntityAI
import java.io.File
import java.lang.Exception
import kotlin.script.experimental.host.StringScriptSource

object AIManager {

    val loadedAIs = mutableMapOf<String, IEntityAI>()

    fun get(scriptSrcPath: String): IEntityAI? = loadedAIs[scriptSrcPath]

    @Throws(NoSuchFileException::class)
    fun load(scriptSrcPath: String): IEntityAI {

        if(scriptSrcPath in loadedAIs)
            return get(scriptSrcPath)!!

        val script = try {
            if(scriptSrcPath == "null") {
                RpgUtt.logger.warn("Script file not specified!")
                throw NoSuchFileException(File(ClassLoader.getSystemResource("assets/rpgutt/script/$scriptSrcPath.kts").toURI()))
            } else {
                ScriptEvaluator.compile(StringScriptSource(ResourceManager.loadScriptSrc(scriptSrcPath)))
            }
        } catch (t: NoSuchFileException) {
            throw t
        } catch (e: Throwable) {
            throw Exception("Failed to load script file! maybe there are some illegal statements in program.")
        }

        val ai = ScriptEvaluator.eval<Any?>(script) as IEntityAI

        loadedAIs[scriptSrcPath] = ai

        return ai

    }

    fun load(dummySrcPath: String, ai: IEntityAI): IEntityAI {

        if(dummySrcPath in loadedAIs)
            return get(dummySrcPath)!!

        loadedAIs[dummySrcPath] = ai

        return ai

    }

}