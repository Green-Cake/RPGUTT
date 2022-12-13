package crpth.util.type

import crpth.util.vec.Vec2i

enum class Direction(val component: Vec2i) {

    SOUTH(Vec2i(0, -1)),
    WEST(Vec2i(-1, 0)),
    EAST(Vec2i(1, 0)),
    NORTH(Vec2i(0, 1)),

}