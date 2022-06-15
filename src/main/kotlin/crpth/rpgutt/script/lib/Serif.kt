package crpth.rpgutt.script.lib

import crpth.util.vec.Vec4b

class Serif(val atoms: List<Atom>, val doAutoPaging: Boolean = false) {

    companion object {
        val EMPTY = Serif()
    }

    constructor(vararg atoms: Atom) : this(atoms.asList())

    data class Atom(val color: Vec4b, val duration: Int, val textLines: Array<out String>) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Atom

            if (!textLines.contentEquals(other.textLines)) return false
            if (color != other.color) return false
            if (duration != other.duration) return false

            return true
        }

        override fun hashCode(): Int {
            var result = textLines.contentHashCode()
            result = 31 * result + color.hashCode()
            result = 31 * result + duration
            return result
        }

    }

}