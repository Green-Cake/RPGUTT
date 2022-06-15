package crpth.util.gui

import crpth.rpgutt.RpgUtt
import crpth.util.type.BoundingBox
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f

class GuiButton(z: Int, pos: Vec2f, size: Vec2f) : GuiNode(z, pos, size) {

    val bb = BoundingBox.fromPosAndSize(pos, size)

    override fun update() {

    }

    override fun render(renderer: Renderer) {

    }

    override fun onClicked(button: MouseButton, action: MouseAction): Boolean {

        val cursor = RpgUtt.cursorPos

        if(cursor !in bb)
            return false

        return true

    }


}