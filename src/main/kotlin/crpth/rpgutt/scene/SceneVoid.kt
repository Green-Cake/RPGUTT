package crpth.rpgutt.scene

import crpth.util.RichWindow
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer

object SceneVoid : IScene {

    override fun init() = Unit

    override fun update() = Unit

    override fun render(renderer: Renderer) = Unit

    override fun reset() = Unit

    override fun onClicked(window: RichWindow, button: MouseButton, action: MouseAction) = false

}