package crpth.util.render

import crpth.rpgutt.ResourceManager
import crpth.util.vec.Vec2i
import org.lwjgl.opengl.GL11

class TileSet(path: String, val sizePerTile: Vec2i) {

    companion object {
        fun createLazyInit(path: String, sizePerTile: Vec2i): Lazy<TileSet> = lazy {
            TileSet(path, sizePerTile)
        }
    }

    val ids: IntArray
    val width: Int
    val length: Int get() = ids.size

    val countY get() = length / width

    init {

        val idsMut = mutableListOf<Int>()

        val (buffers, width) = ResourceManager.loadSeparatedImagesWithWidth(ClassLoader.getSystemResourceAsStream(path)!!, sizePerTile)

        this.width = width

        buffers.forEach { buf ->

            val id = GL11.glGenTextures()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                sizePerTile.x,
                sizePerTile.y,
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                buf
            )

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
            idsMut += id

        }

        this.ids = idsMut.toIntArray()

    }

    operator fun get(index: Int) = getAsTextureAt(index)

    operator fun get(x: Int, y: Int) = getAsTextureAt(x, y)

    fun getAsTextureAt(index: Int): Texture {
        return Texture(ids[index])
    }

    fun getAsTextureAt(x: Int, y: Int) = getAsTextureAt(y * width + x)

    fun bind(index: Int) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ids[index])
    }

    fun debind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }

    fun delete() {

        ids.forEach(GL11::glDeleteTextures)

    }

}