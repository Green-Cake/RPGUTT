package crpth.util.gui

import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f
import crpth.util.vec.Vec4b

class GuiText(z: Int, pos: Vec2f, size: Vec2f, val text: String, val colorFill: Vec4b) : GuiNode(z, pos, size) {

    override fun update() = Unit

    override fun render(renderer: Renderer) {
        TODO("Not yet implemented")
    }

    override fun onClicked(button: MouseButton, action: MouseAction): Boolean {
        TODO("Not yet implemented")
    }

}