package crpth.rpgutt.entity

import crpth.rpgutt.scene.ISceneStage
import crpth.util.render.Renderer
import crpth.util.render.TileSet
import crpth.util.vec.*
import java.io.DataInputStream
import java.io.DataOutputStream

class EntityMovable(val texture: String, val sizePerTile: Vec2i, pos: GamePos, size: Vec2f) : EntityObject(pos, size) {

    companion object {

        fun decode(stream: DataInputStream): EntityMovable {
            return EntityMovable(stream.readString(), Vec2i.readFrom(stream), GamePos.readFrom(stream), Vec2f.readFrom(stream))
        }

    }

    val textures by TileSet.createLazyInit("assets/rpgutt/textures/entity/$texture.png", sizePerTile)

    override fun render(sceneMain: ISceneStage, renderer: Renderer) {
        renderer.renderTexture(textures[0], sceneMain.getActualPos(pos), sceneMain.getActualSize(size))
    }

    override fun update(sceneStage: ISceneStage): IEntity.Feedback {
        return IEntity.Feedback.CONTINUE
    }

    override fun encode(stream: DataOutputStream) {

        stream.writeString(texture)
        stream.writeLong(sizePerTile.data.toLong())
        pos.encode(stream)
        stream.writeLong(size.data.toLong())

    }
}