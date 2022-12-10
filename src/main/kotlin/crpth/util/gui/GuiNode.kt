package crpth.util.gui

import crpth.util.RichWindow
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f

abstract class GuiNode(val z: Int, val pos: Vec2f, val size: Vec2f): Comparable<GuiNode> {

    abstract fun update()

    abstract fun render(renderer: Renderer)

    abstract fun onClicked(window: RichWindow, button: MouseButton, action: MouseAction): Boolean

    override fun compareTo(other: GuiNode) = z.compareTo(other.z)

}