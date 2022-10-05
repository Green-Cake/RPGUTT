package crpth.rpgutt

import crpth.rpgutt.entity.*
import crpth.rpgutt.map.TileMap
import crpth.rpgutt.map.TileMapGenerator
import crpth.rpgutt.scene.SceneMain
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
        else -> {
            RpgUtt.run()
        }
    }
}

fun order(vararg entities: IEntity) = EntityOrderedList(entities)

fun parallel(vararg entities: IEntity) = EntityParallel(entities.toMutableList())

fun waits(duration: Float) = EntityWait(duration)

@ExperimentalUnsignedTypes
fun compile() {

    val pre = parallel(
        EntitySceneConfig(SceneMain.Parameter.TILE_ID_VOID, 2u)
//        order(
//            EntityTextLine("統一歴3年", Vec2f(0f, 0f), 0.10f, Vec4b.WHITE, 4.0f, 2.0f, 2.0f),
//            parallel(
//                order(
//                    EntityTextLine("大陸を統一した", Vec2f(0f, 0.15f), 0.1f, Vec4b.WHITE, 2.0f, 1f, 1f)
//                ),
//                order(
//                    waits(4.0f),
//                    EntityTextLine("「アトラントの帝国」は", Vec2f(0f, -0.15f), 0.1f, Vec4b.WHITE, 2.0f, 0.5f, 0.5f)
//                )
//            ),
//            waits(0.5f),
//            EntityTextLine("長きに渡った戦争も終わり", Vec2f(0f, 0f), 0.1f, Vec4b.WHITE, 2.0f, 0.5f, 0.5f),
//            waits(0.5f),
//            EntityTextLine("繁栄の絶頂にあった...", Vec2f(0f, 0f), 0.1f, Vec4b.WHITE, 2.0f, 0.5f, 0.5f)
//        )
    )

    val post = parallel(
        EntityPlayer(GamePos(0u, 0u), Vec2f(1f, 1f), Direction4.NORTH),
        EntityPerson("arrow", Vec2i(16, 16), GamePos(7u, 7u), Vec2f(1f, 1f), Direction4.SOUTH, "entity/test")
    )

    val map = TileMap("temporary", Vec2s(100, 100), UShortArray(100*100) { if(Random.nextInt(16) == 0) 2u.toUShort() else 3u.toUShort() }, listOf(pre.createFactory(), post.createFactory()))

    val path = Paths.get("./test.level")

    val generated = TileMapGenerator.generate(map)

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