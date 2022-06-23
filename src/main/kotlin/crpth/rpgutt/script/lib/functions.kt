package crpth.rpgutt.script.lib

import crpth.rpgutt.entity.EntityOrderedList
import crpth.rpgutt.entity.EntityParallel
import crpth.rpgutt.entity.EntityWait
import crpth.rpgutt.entity.IEntity
import crpth.util.logging.Logger
import crpth.util.vec.Vec4b
import kotlin.math.roundToInt

val BLACK = Vec4b.BLACK
val WHITE = Vec4b.WHITE

@JvmInline
value class DurationPerChar(val value: Int)

@JvmInline
value class Second(val value: Float)

@JvmInline
value class Frame(val value: Int)

val currentTime get() = Logger.getTimestamp()

fun perChar(value: Int) = DurationPerChar(value)

fun sec(value: Float) = Second(value)

fun frame(value: Int) = Frame(value)

fun atom(color: Vec4b, durationPerChar: DurationPerChar, vararg textLines: String) = Serif.Atom( color, durationPerChar.value*textLines.sumOf { it.length }, textLines)

fun atom(color: Vec4b, duration: Frame, vararg textLines: String) = Serif.Atom(color, duration.value, textLines)

fun atom(color: Vec4b, durationSec: Second, vararg textLines: String) = Serif.Atom(color, (durationSec.value*60).roundToInt(), textLines)

fun color(r: Int, g: Int, b: Int, a: Int=255) = Vec4b(r, g, b, a)

fun color(r: Float, g: Float, b: Float, a: Float=1f) = Vec4b((255*r).toInt(), (255*g).toInt(), (255*b).toInt(), (255*a).toInt())

fun grayscale(value: Int, a: Int=255) = color(value, value, value, a)

fun grayscale(value: Float, a: Float=1f) = color(value, value, value, a)

/*
atom("Hello!" color(0, 0, 0))
 */

fun order(vararg entities: IEntity) = EntityOrderedList(entities)

fun parallel(vararg entities: IEntity) = EntityParallel(entities)

fun waits(duration: Float) = EntityWait(duration)