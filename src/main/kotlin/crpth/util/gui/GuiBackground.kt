package crpth.util.gui

import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f
import crpth.util.vec.Vec4b

class GuiBackground(z: Int, pos: Vec2f, size: Vec2f, val colorFill: Vec4b, val colorStroke: Vec4b) : GuiNode(z, pos, size) {

    override fun update() = Unit

    override fun render(renderer: Renderer) {

        renderer.color4b(colorFill)
        renderer.drawSquare(pos, size)

        renderer.lineWidth = 2.0f
        renderer.lineSmooth = true

        renderer.color4b(colorStroke)
        renderer.drawLineSquare(pos, size)

    }

    override fun onClicked(button: MouseButton, action: MouseAction) = false
}