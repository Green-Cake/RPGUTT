package crpth.rpgutt.entity

import crpth.rpgutt.scene.ISceneStage
import crpth.rpgutt.script.lib.Serif

interface IEntityTalkable {

    fun getSerif(sceneMain: ISceneStage): Serif?

    fun isTalkable(sceneMain: ISceneStage, player: EntityPlayer): Boolean

}