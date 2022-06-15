package crpth.util.gui

import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer

class Gui {

    val nodes = sortedSetOf<GuiNode>()

    fun update() {
        nodes.forEach { it.update() }
    }

    fun render(renderer: Renderer) {
        nodes.forEach { it.render(renderer) }
    }

    fun onClicked(button: MouseButton, action: MouseAction): Boolean {
        nodes.forEach {
            if(it.onClicked(button, action))
                return true
        }
        return false
    }

}