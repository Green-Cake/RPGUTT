package crpth.util.experimental

import crpth.util.vec.Vec2f
import crpth.util.vec.Vec2i
import org.lwjgl.stb.STBTTAlignedQuad
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.stackPush
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path


class TruetypeFont(path: Path) {

    val height = 12f

    val ttf = ByteBuffer.wrap(Files.readAllBytes(path))

    val info = STBTTFontinfo.create().apply {
        stbtt_InitFont(this, ttf)
    }

    val ascent: Int
    val descent: Int
    val lineGap: Int

    init {

        MemoryStack.stackPush().use {

            val pAscent = it.mallocInt(1)
            val pDescent = it.mallocInt(1)
            val pLineGap = it.mallocInt(1)

            stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap)

            ascent = pAscent[0]
            descent = pDescent[0]
            lineGap = pLineGap[0]

        }

    }

    fun renderText(cdata: STBTTBakedChar.Buffer, bitmapSize: Vec2i) {

        val scale = stbtt_ScaleForPixelHeight(info, height)

        MemoryStack.stackPush().use {

            val pCodePoint = it.mallocInt(1)

            val x = it.floats(0f)
            val y = it.floats(0f)

            val q = STBTTAlignedQuad.malloc(it)

            var lineStart = 0

        }

    }

}