package crpth.rpgutt.scene

import crpth.rpgutt.RpgUtt
import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.render.Renderer

abstract class SceneParent : IScene {

    var currentSub: Int = -1
        private set

    protected val subScenes = mutableMapOf<Int, SubScene>()

    override fun getActualName(name: String) = "$resourceUserId:$name"

    fun addSubScene(sub: SubScene) {
        subScenes[sub.id] = sub
    }

    override fun update() {

        subScenes[currentSub]?.update()

    }

    override fun render(renderer: Renderer) {

        subScenes[currentSub]?.render(renderer)

    }

    fun changeSub(id: Int, reset: Boolean=true) {

        RpgUtt.logger.fine("changed sub-scene from ${(subScenes[currentSub] ?: SceneVoid)::class.simpleName} to ${(subScenes[id] ?: SceneVoid)::class.simpleName}${if(reset)" with reset" else ""}.")

        currentSub = id

        if(reset)
            subScenes[currentSub]?.reset()
    }

    override fun onClicked(button: MouseButton, action: MouseAction): Boolean {

        return subScenes[currentSub]?.onClicked(button, action) == true

    }

}