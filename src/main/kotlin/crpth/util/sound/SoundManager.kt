package crpth.util.sound

import crpth.rpgutt.IResourceUser
import crpth.rpgutt.RpgUtt
import javafx.scene.media.AudioClip

class SoundManager {

    val oggManager = OggManager()

    val audios = mutableMapOf<String, AudioClip>()

    fun init() {
        oggManager.init()
    }

    fun finish() {
        oggManager.finish()
        audios.values.forEach {
            it.stop()
        }
        audios.clear()
    }

    fun removeUserResources(user: IResourceUser) {

        audios.keys.filter { it.startsWith(user.resourceUserId+":") }.forEach(::free)
        oggManager.sounds.keys.filter { it.startsWith(user.resourceUserId+":") }.forEach(::free)

    }

    operator fun contains(name: String) = name in audios || name in oggManager

    fun load(user: IResourceUser, name: String, path: String, doLoop: Boolean) = RpgUtt.soundManager.load("${user.resourceUserId}:$name", path, doLoop)

    fun load(name: String, path: String, doLoop: Boolean): String {

        if(path.endsWith(".ogg")) {

            oggManager.loadOgg(name, path, doLoop)

            return name
        }

        audios[name] = AudioClip(ClassLoader.getSystemResource("assets/rpgutt/sounds/$path").toString()).apply {
            cycleCount = if(doLoop) AudioClip.INDEFINITE else 1
        }

        return name

    }

    fun free(name: String) {

        audios.remove(name)
        oggManager.free(name)

    }

    fun play(name: String, volume: Double=1.0) {

        if(name in oggManager)
            oggManager.play(name, volume.toFloat())
        else
            audios[name]?.play(volume)

    }

    fun play(user: IResourceUser, name: String, volume: Double=1.0) = play(user.getActualName(name), volume)

    fun playRandom(vararg names: String, volume: Double=1.0) = play(names.random(), volume)

    fun playRandom(user: IResourceUser, vararg names: String, volume: Double=1.0) = play(user, names.random(), volume)

    fun stop(name: String) {
        audios[name]?.stop()
        oggManager.stop(name)
    }

    fun stop(user: IResourceUser, name: String) {
        audios[user.getActualName(name)]?.stop()
    }

    fun setVolume(name: String, volume: Double) {

        audios[name]?.volume = volume
        oggManager.setVolume(name, volume.toFloat())

    }

}