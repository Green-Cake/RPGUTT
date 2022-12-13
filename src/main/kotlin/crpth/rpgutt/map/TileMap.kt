package crpth.rpgutt.map

import crpth.util.vec.Vec2s

class TileMap(val name: String, val size: Vec2s, val tileSets: List<String>, val tiles: Array<UShortArray>, val entityFactories: List<EntityFactory>) {

    val layerCount get() = tiles.size

    operator fun get(layer: Int, x: Int, y: Int) = tiles[layer][y * size.x + x]

    operator fun set(layer: Int, x: Int, y: Int, value: UShort) {
        tiles[layer][y * size.x + x] = value
    }

}