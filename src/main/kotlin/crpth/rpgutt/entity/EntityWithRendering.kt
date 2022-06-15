package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.util.render.Renderer

abstract class EntityWithRendering : IEntity {

    abstract fun render(sceneMain: SceneMain, renderer: Renderer)

}