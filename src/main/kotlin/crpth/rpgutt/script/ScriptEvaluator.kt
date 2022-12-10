package crpth.rpgutt.script

import crpth.rpgutt.entity.ai.EntityParams
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object ScriptEvaluator {

    val compilationConfig = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(
                wholeClasspath = true
            )
        }
        defaultImports("crpth.rpgutt.script.lib.*", "crpth.util.vec.vec.*", "kotlin.random.*", "crpth.rpgutt.entity.ai.*")
    }

    val host = BasicJvmScriptingHost()

    fun compile(scriptFile: SourceCode): CompiledScript {

        val compiled = host.runInCoroutineContext { BasicJvmScriptingHost().compiler(scriptFile, compilationConfig) }

        return compiled.valueOr { throw Exception(it.reports.joinToString { r -> r.render() }) }
    }

    inline fun <reified T> eval(script: CompiledScript): T? {

        val evaluationConfiguration = ScriptEvaluationConfiguration {
            implicitReceivers()
        }

        val res = host.runInCoroutineContext { host.evaluator(script, evaluationConfiguration) }

        return (res.valueOr { return null }.returnValue as? ResultValue.Value)?.value as? T
    }

}