package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer
import crpth.util.type.BoundingBox

abstract class EntityWithRendering : IEntity {

    abstract fun render(sceneMain: SceneMain, renderer: Renderer)

    open fun isRenderingTarget(sceneMain: SceneMain, renderer: Renderer, bound: BoundingBox): Boolean = true

}