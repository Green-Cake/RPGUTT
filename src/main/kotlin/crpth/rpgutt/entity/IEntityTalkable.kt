package crpth.rpgutt.entity

import crpth.rpgutt.scene.SceneMain
import crpth.rpgutt.script.lib.Serif

interface IEntityTalkable {

    fun getSerif(sceneMain: SceneMain): Serif?

    fun isTalkable(sceneMain: SceneMain, player: EntityPlayer): Boolean

}