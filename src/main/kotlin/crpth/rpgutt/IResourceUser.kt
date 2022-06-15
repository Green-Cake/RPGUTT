package crpth.rpgutt

interface IResourceUser {

    val resourceUserId: String get() = this::class.simpleName!!

    fun getActualName(name: String): String = "$resourceUserId:$name"

    fun loadSound(name: String, path: String, doLoop: Boolean) {

        RpgUtt.soundManager.load(getActualName(name), path, doLoop)

    }

    fun setVolume(name: String, volume: Double) {
        RpgUtt.soundManager.setVolume(getActualName(name), volume)
    }

    fun freeResources() {

        RpgUtt.soundManager.removeUserResources(this)

    }

    fun play(name: String, volume: Double=1.0) = RpgUtt.soundManager.play(getActualName(name), volume)

    fun playRandom(vararg names: String, volume: Double=1.0) = RpgUtt.soundManager.playRandom(this, *names, volume=volume)

    fun stop(name: String) = RpgUtt.soundManager.stop(this, name)

}