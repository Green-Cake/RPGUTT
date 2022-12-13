package crpth.util.gui

import crpth.util.RichWindow
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer
import crpth.util.vec.Vec2f

/**
 * @param z a larger value means that the node covers the node with the smaller value.
 */
abstract class GuiNode(val z: Int, val pos: Vec2f, val size: Vec2f): Comparable<GuiNode> {

    abstract fun update()

    abstract fun render(renderer: Renderer)

    abstract fun onClicked(window: RichWindow, button: MouseButton, action: MouseAction): Boolean

    override fun compareTo(other: GuiNode) = z.compareTo(other.z)

}