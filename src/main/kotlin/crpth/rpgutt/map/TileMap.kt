package crpth.rpgutt.map

import crpth.util.vec.Vec2s

data class TileMap(val name: String, val size: Vec2s, val tiles: UShortArray, val entityFactories: List<EntityFactory>) {

    operator fun get(x: Int, y: Int) = tiles[y * size.x + x]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TileMap

        if (name != other.name) return false
        if (size != other.size) return false
        if (!tiles.contentEquals(other.tiles)) return false
        if (entityFactories != other.entityFactories) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + tiles.contentHashCode()
        result = 31 * result + entityFactories.hashCode()
        return result
    }

}