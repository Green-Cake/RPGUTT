package crpth.rpgutt

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.extension

object SavedDataManager {

    val PATH = Paths.get("./save/")
    const val EXTENSION = "save"

    fun findSavedData() = Files.list(PATH).filter { it.extension == EXTENSION }

    fun countAvailableSavedData() = findSavedData().count()

    fun save() {



    }

}