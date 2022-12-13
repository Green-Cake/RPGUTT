package crpth.util.render.font

import crpth.util.render.Texture
import org.lwjgl.stb.STBEasyFont
import java.awt.Color
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

object FontLoader {

    private val ctx = FontRenderContext(AffineTransform(), true, true)

    fun load(chars: CharArray, font: Font): TextureCharacters {

        val textureMap = chars.associateWith {

            val bound = font.getStringBounds(it.toString(), ctx)

            val img = BufferedImage(bound.width.toInt(), bound.height.toInt(), BufferedImage.TYPE_INT_ARGB)

            val graphics = img.createGraphics()

            graphics.font = font

            graphics.color = Color.WHITE

            graphics.drawString(it.toString(), 0, img.height - (graphics.fontMetrics.leading + graphics.fontMetrics.descent)/2)

            Texture.load(img)

        }

        return TextureCharacters(font, textureMap.toMutableMap())

    }

    fun load(chars: Array<out CharRange>, font: Font) =
        load(buildString {
            for (range in chars) for (c in range) {
                append(c)
            }
        }.toCharArray(), font)

    fun load(char: Char, font: Font) = load(charArrayOf(char), font)

}