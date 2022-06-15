package crpth.util.render

import org.lwjgl.opengl.GL11

interface ITexture {

    val id: Int

    /**
     * Be careful! this function works only when the texture has been bound; otherwise never call this.
     */
    fun getWidth(): Int = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)

    /**
     * Be careful! this function works only when the texture has been bound; otherwise never call this.
     */
    fun getHeight(): Int = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)

    /**
     * Be careful! this function works only when the texture has been bound; otherwise never call this.
     */
    fun getAspectRatio() = getWidth().toDouble() / getHeight().toDouble()

    fun bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
    }

    fun debind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }

    fun delete() {
        GL11.glDeleteTextures(id)
    }

    fun <R> use(block: ITexture.()->R): R

}