package crpth.util.render

import crpth.util.render.font.FontManager
import crpth.util.render.font.TextureCharacters
import crpth.util.vec.*
import org.lwjgl.opengl.GL11.*
import java.awt.Font

class Renderer {

    var doDrawOverline = false
    var doDrawUnderline = false

    val fontManager = FontManager(16)

    val fontEn = fontManager.fontMonospaced
    val fontJa = fontManager.fontYumincho

    fun color4b(color: Vec4b) {
        glColor4f(color.r.resizeToInt()/255f, color.g.resizeToInt()/255f,  color.b.resizeToInt()/255f, color.a.resizeToInt()/255f)
    }

    inline fun draw(mode: Int, block: ()->Unit) {
        glBegin(mode)
        block()
        glEnd()
    }

    fun drawSquare(position: IVec2f, size: IVec2f)  = draw(GL_QUADS) {
        glVertex2f(position.x, position.y + size.y)
        glVertex2f(position.x, position.y)
        glVertex2f(position.x + size.x, position.y)
        glVertex2f(position.x + size.x, position.y + size.y)
    }

    fun drawScreen() = drawSquare(Vec2f(-1f, -1f), Vec2f(2f, 2f))

    fun renderLineStrip(vararg positions: Vec2d) {

        draw(GL_LINE_STRIP) {
            positions.forEach {
                glVertex2d(it.x, it.y)
            }
        }

    }

    fun renderTexture(texture: ITexture, position: Vec2d, size: Vec2d, srcStart: Vec2d = Vec2d.ZERO, srcEnd: Vec2d = Vec2d.ONE, initColor: Vec4f?=null) {

        if(initColor != null)
            glColor4f(initColor.r, initColor.g, initColor.b, initColor.a)

        texture.bind()

        draw(GL_QUADS) {

            glTexCoord2d(srcStart.x, srcStart.y)
            glVertex2d(position.x, position.y + size.y)

            glTexCoord2d(srcStart.x, srcEnd.y)
            glVertex2d(position.x, position.y)

            glTexCoord2d(srcEnd.x, srcEnd.y)
            glVertex2d(position.x + size.x, position.y)

            glTexCoord2d(srcEnd.x, srcStart.y)
            glVertex2d(position.x + size.x, position.y + size.y)

        }

        texture.debind()

    }

    fun renderTexture(texture: ITexture, position: Vec2d, height: Double, width: Double, srcStart: Vec2d = Vec2d.ZERO, srcEnd: Vec2d = Vec2d.ONE) {

        texture.bind()

        draw(GL_QUADS) {

            glTexCoord2d(srcStart.x, srcStart.y)
            glVertex2d(position.x, position.y + height)

            glTexCoord2d(srcStart.x, srcEnd.y)
            glVertex2d(position.x, position.y)

            glTexCoord2d(srcEnd.x, srcEnd.y)
            glVertex2d(position.x + width, position.y)

            glTexCoord2d(srcEnd.x, srcStart.y)
            glVertex2d(position.x + width, position.y + height)

        }

        texture.debind()

    }

    /**
     * AA means Auto Aspect
     * @return width value calculated and used for rendering.
     */
    fun renderTextureAA(texture: ITexture, position: Vec2d, height: Double, srcStart: Vec2d = Vec2d.ZERO, srcEnd: Vec2d = Vec2d.ONE): Double {

        texture.bind()

        val aspect = texture.getAspectRatio()

        draw(GL_QUADS) {

            glTexCoord2d(srcStart.x, srcStart.y)
            glVertex2d(position.x, position.y + height)

            glTexCoord2d(srcStart.x, srcEnd.y)
            glVertex2d(position.x, position.y)

            glTexCoord2d(srcEnd.x, srcEnd.y)
            glVertex2d(position.x + height * aspect, position.y)

            glTexCoord2d(srcEnd.x, srcStart.y)
            glVertex2d(position.x + height * aspect, position.y + height)

        }

        texture.debind()

        return height * aspect

    }

    /**
     * @return the width of the rendered character.
     */
    fun renderChar(char: Char, tex: TextureCharacters, position: Vec2d, height: Double): Double {

        val i = tex.chars.indexOf(char)

        return renderTextureAA(Texture(tex.ids[i]), position, height)

    }

    fun getTcOrDynamicallyGen(char: Char, dfont: Font=fontJa) = when(char) {

        in fontManager.textures[fontEn]!!.chars ->fontManager.textures[fontEn]!!
        in fontManager.textures[fontJa]!!.chars -> fontManager.textures[fontJa]!!
        else -> {
            fontManager.textures[dfont]!!.dynamicLoad(char)
            fontManager.textures[dfont]!!
        }

    }

    fun getCharAR(char: Char, tex: TextureCharacters?): Double {

        val c = (tex ?: getTcOrDynamicallyGen(char)).getCharAsTexture(char)

        c.bind()
        val r = c.getAspectRatio()
        c.debind()

        return r

    }

    fun getStringAR(str: String, tex: TextureCharacters?): Double {

        return str.sumOf { getCharAR(it, tex) }

    }

    fun renderString(str: String, tex: TextureCharacters?, position: Vec2d, height: Double, spacing: Double = 0.0): Double {

        var offset = 0.0

        str.forEach {

            val t = tex ?: getTcOrDynamicallyGen(it)

            offset += renderChar(it, t, position.plus(offset, 0.0), height) + spacing

        }

        if(doDrawOverline) {
            glColor3f(1f, 1f, 1f)
            renderLineStrip(position.plus(0.0, height), position.plus(offset, height))
        }

        if(doDrawUnderline) {
            glColor3f(1f, 1f, 1f)
            renderLineStrip(position.plus(0.0, 0.0), position.plus(offset, 0.0))
        }

        return offset

    }

    fun renderString(str: String, font: Font, position: Vec2d, height: Double, spacing: Double = 0.0) =
        renderString(str, fontManager.textures[font]!!, position, height, spacing)

    fun renderStringCentered(str: String, tex: TextureCharacters?, position: Vec2d, height: Double, spacing: Double = 0.0): Double {

        val pos = position.minus(height*getStringAR(str, tex)/2 + spacing*(str.length-1), height/2)

        return renderString(str, tex, pos, height, spacing)

    }

    fun renderStringCentered(str: String, font: Font, position: Vec2d, height: Double, spacing: Double = 0.0) =
        renderStringCentered(str, fontManager.textures[font]!!, position, height, spacing)

    inline fun matrix(block: ()->Unit) {
        glPushMatrix()
        block()
        glPopMatrix()
    }

    inline fun matrix(translate: Vec3f, rotation: Vec3f, block: ()->Unit) {
        glPushMatrix()

        glTranslatef(translate.x, translate.y, translate.z)

        rotate(rotation)

        block()
        glPopMatrix()
    }

    fun rotate(r: Vec3f) {
        glRotatef(r.x, 1f, 0f, 0f)
        glRotatef(r.y, 0f, 1f, 0f)
        glRotatef(r.z, 0f, 0f, 1f)
    }

    inline fun matrix(m: Matrix, crossinline block: ()->Unit) {
        matrix(m.translate, m.rotate, block)
    }

}