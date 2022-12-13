package crpth.rpgutt.script

import crpth.rpgutt.entity.ai.EntityParams
import crpth.util.vec.GamePos
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
        defaultImports("crpth.rpgutt.script.lib.*", "crpth.util.vec.*", "kotlin.random.*", "crpth.rpgutt.entity.*", "crpth.rpgutt.entity.ai.*")
    }

    val compilationConfigBuilder = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(
                wholeClasspath = true
            )
        }
        implicitReceivers(ImplicitReceiverForBuilder::class)
        defaultImports("crpth.rpgutt.script.lib.*", "crpth.util.vec.*", "kotlin.random.*", "crpth.rpgutt.entity.*", "crpth.rpgutt.entity.ai.*")
    }

    val host = BasicJvmScriptingHost()

    fun compile(scriptFile: SourceCode): CompiledScript {

        val compiled = host.runInCoroutineContext { BasicJvmScriptingHost().compiler(scriptFile, compilationConfig) }

        return compiled.valueOr { throw Exception(it.reports.joinToString { r -> r.render() }) }
    }

    fun compileBuilders(scriptFile: SourceCode): CompiledScript {

        val compiled = host.runInCoroutineContext { BasicJvmScriptingHost().compiler(scriptFile, compilationConfigBuilder) }

        return compiled.valueOr { throw Exception(it.reports.joinToString { r -> r.render() }) }
    }

    inline fun <reified T> eval(script: CompiledScript): T? {

        val evaluationConfiguration = ScriptEvaluationConfiguration {
        }

        val res = host.runInCoroutineContext { host.evaluator(script, evaluationConfiguration) }

        return (res.valueOr { return null }.returnValue as? ResultValue.Value)?.value as? T
    }

    inline fun <reified T> evalBuilderEntity(script: CompiledScript, pos: GamePos): T? {

        val evaluationConfiguration = ScriptEvaluationConfiguration {
            implicitReceivers(ImplicitReceiverForBuilder(pos))
        }

        val res = host.runInCoroutineContext { host.evaluator(script, evaluationConfiguration) }

        return (res.valueOr { return null }.returnValue as? ResultValue.Value)?.value as? T
    }

}

data class ImplicitReceiverForBuilder(val pos: GamePos)

//"rock", vec(16, 16), pos.dropSub(), Vec2f.ONE