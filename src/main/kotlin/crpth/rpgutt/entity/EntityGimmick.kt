package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.vec.*
import org.lwjgl.opengl.GL11
import java.io.DataInputStream
import java.io.DataOutputStream

abstract class EntityGimmick(val texture: String, val sizePerTile: Vec2i, pos: GamePos, size: Vec2f) : EntityObject(pos.withZ(-1), size) {

    val textures by TileSet.createLazyInit("assets/rpgutt/textures/entity/$texture.png", sizePerTile)

    override fun render(sceneMain: SceneMain, renderer: Renderer) {
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
        renderer.renderTexture(textures[0], sceneMain.getActualPos(pos), sceneMain.getActualSize(size))
    }

    override fun encode(stream: DataOutputStream) {

        stream.writeString(texture)
        stream.writeLong(sizePerTile.data.toLong())
        pos.data.encode(stream)
        stream.writeLong(size.data.toLong())

    }
}