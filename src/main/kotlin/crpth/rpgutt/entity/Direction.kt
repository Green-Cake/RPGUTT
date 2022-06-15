package crpth.rpgutt.entity

import crpth.util.vec.Vec2i

enum class Direction(val component: Vec2i) {

    NORTH(Vec2i(0, 1)),
    EAST(Vec2i(1, 0)),
    SOUTH(Vec2i(0, -1)),
    WEST(Vec2i(-1, 0))

}