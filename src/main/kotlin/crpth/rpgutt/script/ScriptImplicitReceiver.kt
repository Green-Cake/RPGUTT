package crpth.rpgutt.script

import crpth.rpgutt.entity.EntityPerson
import crpth.rpgutt.scene.SceneMain

class ScriptImplicitReceiver(val sceneMain: SceneMain, val self: EntityPerson, val situation: String) {

    val GAME_VERSION = "1.0.0"

    val player by sceneMain::player

    val isTalking by sceneMain::isTalking

}