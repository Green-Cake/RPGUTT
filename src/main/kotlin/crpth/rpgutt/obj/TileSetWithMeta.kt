package crpth.rpgutt.obj

import crpth.util.render.TileSet

data class TileSetWithMeta(val tileSet: TileSet, val barriers: Set<UShort>) {

    companion object {

        fun integrate(tileSets: List<TileSetWithMeta>): Pair<IntArray, Set<UShort>> {

            val tileTextureIDs = tileSets.flatMap { it.tileSet.ids.toList() }.toIntArray()

            val barriers = mutableSetOf<UShort>()

            var offset = 0

            tileSets.forEach { swm ->

                try {
                    barriers.addAll(swm.tileSet.ids.map { (it + offset).toUShort() })
                } finally {
                    offset += swm.tileSet.length
                }

            }

            return tileTextureIDs to barriers

        }

    }

}