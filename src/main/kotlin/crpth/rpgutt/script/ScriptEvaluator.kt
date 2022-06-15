package crpth.rpgutt.script

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
        implicitReceivers(ScriptImplicitReceiver::class)
        defaultImports("crpth.rpgutt.script.lib.*", "crpth.util.vec.*", "kotlin.random.*")
    }

    val host = BasicJvmScriptingHost()

    fun compile(scriptFile: SourceCode): CompiledScript {

        val compiled = host.runInCoroutineContext { BasicJvmScriptingHost().compiler(scriptFile, compilationConfig) }

        return compiled.valueOr { throw Exception(it.reports.joinToString { r -> r.render() }) }
    }

    inline fun <reified T> eval(receiver: ScriptImplicitReceiver, script: CompiledScript): T? {

        val evaluationConfiguration = ScriptEvaluationConfiguration {
            implicitReceivers(receiver)
        }

        val res = host.runInCoroutineContext { host.evaluator(script, evaluationConfiguration) }

        return (res.valueOr { return null }.returnValue as? ResultValue.Value)?.value as? T
    }

}