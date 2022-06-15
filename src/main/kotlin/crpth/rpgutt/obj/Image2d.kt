package crpth.rpgutt.obj

import crpth.util.render.ITexture
import crpth.util.render.Renderer
import crpth.util.vec.Vec2d

class Image2d(val texture: ITexture, val position: Vec2d, val size: Vec2d, val srcStart: Vec2d = Vec2d.ZERO, val srcEnd: Vec2d = Vec2d.ONE) {

    fun render(renderer: Renderer) {

        renderer.renderTexture(texture, position, size, srcStart, srcEnd)

    }

}