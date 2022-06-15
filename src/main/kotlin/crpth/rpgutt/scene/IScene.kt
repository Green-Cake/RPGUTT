package crpth.rpgutt.scene

import crpth.rpgutt.IResourceUser
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer

interface IScene : IResourceUser {

    fun init()

    fun update()

    fun render(renderer: Renderer)

    fun reset()

    fun onClicked(button: MouseButton, action: MouseAction): Boolean

}