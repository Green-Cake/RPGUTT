package crpth.util.render.font

import crpth.util.render.TileSet
import crpth.util.vec.Vec2i
import java.awt.Font

object FontManager {

    enum class Chars(val id: Int) {
        QUESTION(0)
    }

    val charMap = mutableMapOf<Char, Int>()

    init {

        var i = 0

        charMap['?'] = i++

        for(c in 'A'..'Z') {
            charMap[c] = i++
        }

    }

    val texture by TileSet.createLazyInit("assets/rpgutt/textures/char/set0.png", Vec2i(16, 16))

    operator fun get(char: Char) = texture[charMap[char] ?: Chars.QUESTION.id]

}