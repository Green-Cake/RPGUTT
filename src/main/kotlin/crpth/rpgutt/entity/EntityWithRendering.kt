package crpth.rpgutt.entity

import crpth.rpgutt.scene.ISceneStage
import crpth.util.render.Renderer
import crpth.util.type.BoundingBox

abstract class EntityWithRendering : IEntity {

    abstract val posZ: Int

    abstract fun render(sceneMain: ISceneStage, renderer: Renderer)

    open fun isRenderingTarget(sceneMain: ISceneStage, renderer: Renderer, bound: BoundingBox): Boolean = true

}