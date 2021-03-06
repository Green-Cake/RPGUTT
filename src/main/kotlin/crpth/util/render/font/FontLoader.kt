package crpth.util.render.font

import crpth.util.render.Texture
import java.awt.Color
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

object FontLoader {

    private val ctx = FontRenderContext(AffineTransform(), true, true)

    fun load(chars: CharArray, font: Font): TextureCharacters {

        val ids = chars.map {

            val bound = font.getStringBounds(it.toString(), ctx)

            val img = BufferedImage(bound.width.toInt(), bound.height.toInt(), BufferedImage.TYPE_INT_ARGB)

            val graphics = img.createGraphics()

            graphics.font = font

            graphics.color = Color.WHITE

            graphics.drawString(it.toString(), 0, img.height - (graphics.fontMetrics.leading + graphics.fontMetrics.descent)/2)

            Texture.load(img).id

        }

        return TextureCharacters(font, chars, ids.toIntArray())

    }

    fun load(chars: Array<out CharRange>, font: Font) =
        load(buildString {
            for (range in chars) for (c in range) {
                append(c)
            }
        }.toCharArray(), font)

    fun load(char: Char, font: Font) = load(charArrayOf(char), font)

}