package crpth.util.mouse

import org.lwjgl.glfw.GLFW

enum class MouseAction(val glfwValue: Int) {

    PRESS(GLFW.GLFW_PRESS),
    RELEASE(GLFW.GLFW_RELEASE)

    ;

    companion object {

        fun from(v: Int) = if(v == PRESS.glfwValue) PRESS else RELEASE

    }

}