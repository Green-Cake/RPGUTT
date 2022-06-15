package crpth.rpgutt.scene

import crpth.rpgutt.RpgUtt

abstract class SubScene(val id: Int, val root: SceneParent) : IScene {

    override fun getActualName(name: String) =
        if("$resourceUserId:$name" in RpgUtt.soundManager)
            "$resourceUserId:$name"
        else
            "${root.resourceUserId}:$name"

    operator fun compareTo(other: SubScene) = id.compareTo(other.id)

    override fun equals(other: Any?) = other is SubScene && id == other.id

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + root.hashCode()
        return result
    }

    //

    override fun play(name: String, volume: Double) {

        if("$resourceUserId:$name" in RpgUtt.soundManager)
            RpgUtt.soundManager.play("$resourceUserId:$name", volume)
        else
            RpgUtt.soundManager.play("${root.resourceUserId}:$name", volume)

    }

    override fun playRandom(vararg names: String, volume: Double) = play(names.random())

    override fun stop(name: String) {
        if("$resourceUserId:$name" in RpgUtt.soundManager)
            RpgUtt.soundManager.stop("$resourceUserId:$name")
        else
            RpgUtt.soundManager.stop("${root.resourceUserId}:$name")
    }

}