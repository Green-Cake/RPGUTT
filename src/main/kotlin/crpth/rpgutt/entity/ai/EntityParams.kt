package crpth.rpgutt.entity.ai

import crpth.rpgutt.entity.EntityPerson
import crpth.rpgutt.scene.SceneMain

class EntityParams(val self: EntityPerson) {

    val GAME_VERSION = "1.0.0"

    val player by SceneMain::player

    val isTalking by SceneMain::isTalking

}