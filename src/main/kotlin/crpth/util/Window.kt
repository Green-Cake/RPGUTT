package crpth.util

import crpth.rpgutt.NULL
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW

@JvmInline
value class Window(val id: Long) {

    companion object {

        fun create(width: Int, height: Int, title: CharSequence, monitor: Long = NULL, share: Long = NULL) = Window(GLFW.glfwCreateWindow(width, height, title, monitor, share))

    }

    fun isNull() = id == NULL

    fun freeCallbacks() = Callbacks.glfwFreeCallbacks(id)

    fun destroy() = GLFW.glfwDestroyWindow(id)

    fun makeContextCurrent() = GLFW.glfwMakeContextCurrent(id)

    fun show() = GLFW.glfwShowWindow(id)

    fun shouldClose() = GLFW.glfwWindowShouldClose(id)

    fun swapBuffers() = GLFW.glfwSwapBuffers(id)

    fun getWindowSize(): Pair<Int, Int> {
        val width = IntArray(1)
        val height = IntArray(1)
        GLFW.glfwGetWindowSize(id, width, height)
        return width.getOrElse(0) { -1 } to height.getOrElse(0) { -1 }
    }

}