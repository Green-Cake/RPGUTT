package crpth.rpgutt

import crpth.rpgutt.entity.*
import crpth.rpgutt.map.TileMap
import crpth.rpgutt.map.TileMapEncoder
import crpth.rpgutt.scene.MapParameter
import crpth.util.type.Direction
import crpth.util.vec.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.random.Random

fun main(args: Array<String>) {

    when {
        "--test" in args -> {
            test()
        }
        "--compile" in args -> {
            compile()
        }
        "--builder" in args -> {
            RpgUtt.runMapBuilder()
        }
        "--new_map" in args -> {
            newMap()
        }
        else -> {
            RpgUtt.run()
        }
    }
}

tailrec fun requestInt(text: String): Int {

    print("$text: ")

    return readln().toIntOrNull() ?: requestInt(text)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun newMap() {

    print("name: ")
    val name = readln()
    val width = requestInt("width")
    val height = requestInt("height")
    print("Enumerate tilesets' name, separating by commas: ")
    val tileSets = readln().split(',').map { it.trim() }
    val tileIdVoid = requestInt("tileID for Void")
    val tileIdToFill = requestInt("tileID to fill")
    val layerCount = requestInt("How many layers?")

    val configIDVoid = EntityMapConfig(MapParameter.TILE_ID_VOID, tileIdVoid.toUInt())

    val entityParallel = EntityParallel(listOf(configIDVoid))

    val tiles = Array(layerCount) {
        if(it == 0)
            UShortArray(width*height) { tileIdToFill.toUShort() }
        else
            UShortArray(width*height) { 0u }
    }

    val factoryPre = entityParallel.createFactory()
    val factoryPost = EntityParallel(listOf(EntityPlayer(GamePos.ZERO, Vec2f.ONE, Direction.NORTH))).createFactory()
    val map = TileMap(name, vec(width, height).toVec2s(), tileSets, tiles, listOf(factoryPre, factoryPost))

    val data = TileMapEncoder.encode(map)

    var i = 0

    while(true) {

        val path = Paths.get("./generated_$i.level")
        if(Files.exists(path))
            ++i
        else
            break

    }
    val path = Paths.get("./generated_$i.level")

    Files.createFile(path)

    val os = Files.newOutputStream(path)

    val zos = ZipOutputStream(os)

    zos.putNextEntry(ZipEntry("main.bin"))
    zos.write(data)

    zos.close()

}

fun order(vararg entities: IEntity) = EntityOrderedList(entities)

fun parallel(vararg entities: IEntity) = EntityParallel(entities.toMutableList())

fun waits(duration: Float) = EntityWait(duration)

@ExperimentalUnsignedTypes
fun compile() {

    val pre = parallel(
        EntityMapConfig(MapParameter.TILE_ID_VOID, 2u)
    )

    val post = parallel(
        EntityPlayer(GamePos.ZERO, Vec2f(1f, 1f), Direction.NORTH),
        EntityPerson("soldier01", Vec2i(32, 32), GamePos.tile(7, 7), Vec2f(1f, 1f), Direction.SOUTH, "entity/test")
    )

    val map = TileMap("temporary", Vec2s(100, 100), listOf("BrightForest-A2"), arrayOf(UShortArray(100*100) { if(Random.nextInt(16) == 0) 97u.toUShort() else 1u.toUShort() }), listOf(pre.createFactory(), post.createFactory()))

    val path = Paths.get("./test.level")

    val generated = TileMapEncoder.encode(map)

    if(!Files.exists(path))
        Files.createFile(path)

    val os = Files.newOutputStream(path)

    val zos = ZipOutputStream(os)

    zos.putNextEntry(ZipEntry("main.txt"))
    zos.write(generated)

    zos.close()

}

fun test() {
}