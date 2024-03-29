package crpth.util

import crpth.rpgutt.NULL
import crpth.util.vec.Vec2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW

@JvmInline
value class Window(val id: Long) {

    companion object {

        val NULL_W = Window(NULL)

        fun create(width: Int, height: Int, title: CharSequence, monitor: Long = NULL, share: Window = NULL_W) = Window(GLFW.glfwCreateWindow(width, height, title, monitor, share.id))

    }

    fun isNull() = id == NULL

    fun freeCallbacks() = Callbacks.glfwFreeCallbacks(id)

    fun destroy() = GLFW.glfwDestroyWindow(id)

    fun makeContextCurrent() = GLFW.glfwMakeContextCurrent(id)

    fun show() = GLFW.glfwShowWindow(id)

    fun shouldClose() = GLFW.glfwWindowShouldClose(id)

    fun swapBuffers() = GLFW.glfwSwapBuffers(id)

    fun getWindowSize(): Vec2i {
        val width = IntArray(1)
        val height = IntArray(1)
        GLFW.glfwGetWindowSize(id, width, height)
        return Vec2i(width.getOrElse(0) { -1 }, height.getOrElse(0) { -1 })
    }

}