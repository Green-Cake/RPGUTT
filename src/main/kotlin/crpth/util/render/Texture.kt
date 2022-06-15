package crpth.util.render

import crpth.rpgutt.ResourceManager
import crpth.util.vec.Vec2i
import org.lwjgl.opengl.GL11.*
import java.awt.image.BufferedImage
import java.io.File
import java.nio.ByteBuffer

@JvmInline
value class Texture(override val id: Int) : ITexture {

    companion object {

        fun load(path: String): Texture {

            val (pixels, size) = ResourceManager.loadTextureImageBufAndSize(ClassLoader.getSystemResourceAsStream("assets/rpgutt/textures/"+path) ?: throw NoSuchFileException(File("assets/rpgutt/textures/"+path)))
            return load(pixels, size)

        }

        fun load(pixels: ByteBuffer, size: Vec2i): Texture {
            val id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size.x, size.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glBindTexture(GL_TEXTURE_2D, 0)
            return Texture(id)
        }

        fun load(img: BufferedImage): Texture {

            val id = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, id)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.width, img.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, ResourceManager.loadTextureImageBufAndSize(img).first)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glBindTexture(GL_TEXTURE_2D, 0)
            return Texture(id)
        }

        fun createLazyInit(path: String): Lazy<Texture> = lazy {
            load(path)
        }

    }

    override fun <R> use(block: ITexture.()->R): R = try {
        bind()
        block(this)
    } finally {
        debind()
    }

}

