package crpth.util.gui

import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer

class Gui {

    var isEnabled = false
        private set

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    val nodes = sortedSetOf<GuiNode>()

    fun update() {
        if(!isEnabled)
            return
        nodes.forEach { it.update() }
    }

    fun render(renderer: Renderer) {
        if(!isEnabled)
            return
        nodes.forEach { it.render(renderer) }
    }

    fun onClicked(button: MouseButton, action: MouseAction): Boolean {
        if(!isEnabled)
            return false
        nodes.forEach {
            if(it.onClicked(button, action))
                return true
        }
        return false
    }

    fun reset() {
        nodes.clear()
    }

}