package crpth.util.render.font

import java.awt.Font

class FontManager(fontsizeToLoad: Int = 256) {

    val textures = mutableMapOf<Font, TextureCharacters>()

    val fontMonospaced = Font(Font.MONOSPACED, Font.PLAIN, fontsizeToLoad)

    val fontYumincho = Font("游明朝", Font.PLAIN, fontsizeToLoad)

    private fun s(c: Char) = c..c

    fun init() {

        load(fontMonospaced, '!'..'~', '¡'..'¿', s(' '))

        load(fontYumincho, '!'..'~', '¡'..'¿', s(' '), 'ぁ'..'ゖ', 'ァ'..'ヿ')

    }

    fun load(font: Font, chars: CharArray) {
        textures[font] = generateTextureCharacters(font, chars)
    }

    fun load(font: Font, vararg chars: CharRange) {
        textures[font] = generateTextureCharacters(font, chars)
    }

    fun generateTextureCharacters(font: Font, characters: CharArray): TextureCharacters {

        return FontLoader.load(characters, font)

    }

    fun generateTextureCharacters(font: Font, chars: Array<out CharRange>): TextureCharacters {

        return FontLoader.load(chars, font)

    }

}