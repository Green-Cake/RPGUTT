package crpth.rpgutt.scene

import crpth.rpgutt.entity.EntityObject
import crpth.rpgutt.entity.EntityParallel
import crpth.util.type.Direction
import crpth.util.vec.GamePos
import crpth.util.vec.Vec2d
import crpth.util.vec.Vec2f
import crpth.util.vec.Vec2i

interface ISceneStage : IScene {

    val isTalking: Boolean

    var entitiesPre: EntityParallel

    var entities: EntityParallel

    val canPlayerMove: Boolean

    fun getActualPos(pos: GamePos): Vec2d

    fun getActualPos(pos: Vec2i): Vec2d

    fun getActualSize(pos: Vec2f): Vec2d

    fun canEntityGoto(entity: EntityObject, direction: Direction): Boolean



}