package crpth.util.render.font

import crpth.util.render.Texture
import java.awt.Font


data class TextureCharacters(val font: Font, var chars: CharArray, var ids: IntArray) {

    fun getCharAsTexture(char: Char): Texture {
        val index = chars.indexOf(char)
        return if(index < 0) dynamicLoad(char) else Texture(ids[index])
    }

    fun dynamicLoad(char: Char): Texture {

        if(char in chars)
            return getCharAsTexture(char)

        val (_, c, i) = FontLoader.load(char, font)

        chars += c
        ids += i

        return Texture(i[0])

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextureCharacters

        if (font != other.font) return false
        if (!chars.contentEquals(other.chars)) return false
        if (!ids.contentEquals(other.ids)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = font.hashCode()
        result = 31 * result + chars.contentHashCode()
        result = 31 * result + ids.contentHashCode()
        return result
    }

}