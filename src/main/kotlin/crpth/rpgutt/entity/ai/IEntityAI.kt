package crpth.rpgutt.entity.ai

import crpth.rpgutt.script.lib.Serif

interface IEntityAI {

    val updateType: UpdateType

    fun getSerif(params: EntityParams): Serif

    fun update(params: EntityParams)

}