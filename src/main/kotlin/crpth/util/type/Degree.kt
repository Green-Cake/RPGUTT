package crpth.util.type

import kotlin.math.PI

@JvmInline
value class Degree private constructor(val value: Double) {

    companion object {

        fun of(degree: Double) = Degree(degree % 180)

        fun of(radian: Radian) = of(radian.value / PI * 180)

    }

}