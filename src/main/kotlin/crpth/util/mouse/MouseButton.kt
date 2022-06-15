package crpth.util.mouse

import org.lwjgl.glfw.GLFW

enum class MouseButton(val glfwValue: Int) {

    LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
    MIDDLE(GLFW.GLFW_MOUSE_BUTTON_MIDDLE),
    RIGHT(GLFW.GLFW_MOUSE_BUTTON_RIGHT)

    ;

    companion object {

        fun from(v: Int): MouseButton = when(v) {
            LEFT.glfwValue -> LEFT
            MIDDLE.glfwValue -> MIDDLE
            RIGHT.glfwValue -> RIGHT
            else -> throw Exception("Illegal value specified!")
        }

    }

}