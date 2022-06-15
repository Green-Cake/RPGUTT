package crpth.util.sound

import crpth.rpgutt.NULL
import crpth.rpgutt.ResourceManager
import org.lwjgl.BufferUtils
import org.lwjgl.openal.AL
import org.lwjgl.openal.AL10
import org.lwjgl.openal.AL10.AL_PLAYING
import org.lwjgl.openal.AL10.AL_SOURCE_STATE
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.stb.STBVorbis
import org.lwjgl.stb.STBVorbisInfo
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.concurrent.thread

/**
 * The ogg file type sound manager.
 *
 * [SoundManager] can only handle .wav, .mp3 or .mp4 type mainly and cannot change the volume or any parameters of the sound being played.
 *
 * this [OggManager] actually deals with OpenAL so this can do them.
 */
class OggManager {

    var canPlaySound: Boolean = false

    var device: Long = NULL
        private set

    var context: Long = NULL
        private set

    val sounds = mutableMapOf<String, Sound>()

    fun init() {

        device = ALC10.alcOpenDevice(null as ByteBuffer?)

        canPlaySound = device != NULL

        val caps = ALC.createCapabilities(device)

        context = ALC10.alcCreateContext(device, null as IntBuffer?)
        ALC10.alcMakeContextCurrent(context)
        AL.createCapabilities(caps)

    }

    operator fun contains(name: String) = name in sounds

    fun loadOgg(name: String, path: String, doLoop: Boolean) = loadOgg(name, ResourceManager.loadSoundFile(path), doLoop)

    fun loadOgg(name: String, data: ByteBuffer, doLoop: Boolean) {

        val buffer = AL10.alGenBuffers()
        val source = AL10.alGenSources()

        val info = STBVorbisInfo.malloc()
        val error = BufferUtils.createIntBuffer(1)
        val decoder = STBVorbis.stb_vorbis_open_memory(data, error, null)

        if(decoder == NULL)
            throw IOException("Can't open OGG Vorbis! error: ${error[0]}")

        STBVorbis.stb_vorbis_get_info(decoder, info)

        val channels = info.channels()
        val length = STBVorbis.stb_vorbis_stream_length_in_samples(decoder)

        val pcm = BufferUtils.createShortBuffer(length * 2)

        STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm)
        STBVorbis.stb_vorbis_close(decoder)

        AL10.alBufferData(buffer, AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate())

        AL10.alSourcei(source, AL10.AL_BUFFER, buffer)
        AL10.alSourcei(source, AL10.AL_LOOPING, if(doLoop) AL10.AL_TRUE else AL10.AL_FALSE)

        sounds[name] = Sound(buffer, source)

        info.close()

    }

    fun setVolume(name: String, volume: Float) {
        if(name !in sounds)
            return

        AL10.alSourcef(sounds[name]!!.source, AL10.AL_GAIN, volume)
    }

    fun play(name: String, volume: Float): Thread? {
        setVolume(name, volume)
        return play(name)
    }

    fun play(name: String): Thread? {

        if(name !in sounds)
            return null

        val t = thread {

            val source = sounds[name]!!.source

            AL10.alSourcePlay(source)
            var state: Int = AL10.alGetSourcei(source, AL_SOURCE_STATE)
            while (state == AL_PLAYING) {
                Thread.sleep(10)
                state = AL10.alGetSourcei(source, AL_SOURCE_STATE)
            }

            AL10.alSourceStop(source)

        }

        return t

    }

    fun stop(name: String) {

        if(name !in sounds)
            return

        AL10.alSourceStop(sounds[name]!!.source)

    }

    fun playRandom(vararg names: String) {

        play(names.random())

    }

    fun free(name: String) {

        if(name !in sounds)
            return

        val (buffer,source) = sounds[name]!!
        AL10.alDeleteSources(source)
        AL10.alDeleteBuffers(buffer)

    }

    fun finish() {

        sounds.values.forEach {
            AL10.alDeleteSources(it.source)
            AL10.alDeleteBuffers(it.buffer)
        }

        device = ALC10.alcGetContextsDevice(context)
        ALC10.alcMakeContextCurrent(NULL)
        ALC10.alcDestroyContext(context)
        ALC10.alcCloseDevice(device)

    }

}