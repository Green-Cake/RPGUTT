package crpth.util.logging

import java.io.PrintStream
import java.util.*
import kotlin.reflect.KClass

class Logger(val clazz: KClass<*>, val out: PrintStream=System.out, val err: PrintStream=System.err) {

    companion object {

        fun getTimestamp(): String {
            val calendar = Calendar.getInstance()
            return String.format("%02d:%02d:%02d.%03d %d/%d/%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE))
        }

    }

    enum class Level(val value: Int, val isError:Boolean=false) {
        OFF(Int.MAX_VALUE),
        SEVERE(1000, true),
        WARN(900, true),
        INFO(800),
        CONFIG(700),
        FINE(500),
        FINER(400),
        FINEST(300),
        ALL(Int.MIN_VALUE)
    }

    var outputLevel: Level = Level.CONFIG

    private fun log(level: Level, msg: String, s: StackTraceElement, vararg args: Any?): Boolean {

        if(outputLevel.value > level.value)
            return false

        val timestamp = getTimestamp()
        (if(level.isError) err else out).printf("[$timestamp $s] $level: $msg${System.lineSeparator()}", *args)

        return true

    }

    fun finest(msg: String, vararg args: Any?) = log(Level.FINEST, msg, Exception().stackTrace[1], *args)

    fun finer(msg: String, vararg args: Any?) = log(Level.FINER, msg, Exception().stackTrace[1], *args)

    fun fine(msg: String, vararg args: Any?) = log(Level.FINE, msg, Exception().stackTrace[1], *args)

    fun config(msg: String, vararg args: Any?) = log(Level.CONFIG, msg, Exception().stackTrace[1], *args)

    fun info(msg: String, vararg args: Any?) = log(Level.INFO, msg, Exception().stackTrace[1], *args)

    fun warn(msg: String, vararg args: Any?) = log(Level.WARN, msg, Exception().stackTrace[1], *args)

    fun severe(msg: String, vararg args: Any?) = log(Level.SEVERE, msg, Exception().stackTrace[1], *args)

}