package crpth.util.render.font

import crpth.util.render.Texture
import java.awt.Font


data class TextureCharacters(val font: Font, val textureMap: MutableMap<Char, Texture> = mutableMapOf()) {

    fun getCharAsTexture(char: Char): Texture = textureMap[char] ?: dynamicLoad(char)

    fun dynamicLoad(char: Char): Texture {

        if(char in textureMap)
            return getCharAsTexture(char)

        val (_, map) = FontLoader.load(char, font)

        textureMap += map

        return Texture(map[char]?.id ?: 0)

    }

}