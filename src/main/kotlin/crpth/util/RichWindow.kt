package crpth.util

import crpth.util.mouse.MouseAction
import crpth.util.mouse.MouseButton
import crpth.util.vec.Vec2f
import org.lwjgl.glfw.GLFW

class RichWindow(val window: Window, var fOnClicked: (button: MouseButton, action: MouseAction)->Boolean) {

    private val keyMapPrev = mutableSetOf<Int>()
    private val keyMap = mutableSetOf<Int>()

    var cursorPos = Vec2f.ZERO
        private set

    fun init() {

        val windowSize = window.getWindowSize()

        GLFW.glfwSetKeyCallback(window.id, ::onKeyEvent)

        GLFW.glfwSetCursorPosCallback(window.id) { _, x, y ->
            cursorPos = Vec2f(x.toFloat() / windowSize.x, 1.0f - y.toFloat() / windowSize.y) * 2.0f - Vec2f.ONE
        }

        GLFW.glfwSetMouseButtonCallback(window.id) { _, button, action, _, ->
            onClicked(MouseButton.from(button), MouseAction.from(action))
        }

        val cx = DoubleArray(1)
        val cy = DoubleArray(1)
        GLFW.glfwGetCursorPos(window.id, cx, cy)
        cursorPos = Vec2f(cx[0].toFloat()/ windowSize.x, 1.0f - cy[0].toFloat()/ windowSize.y)*2f - Vec2f.ONE

    }

    fun onKeyEvent(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {

        if(action == GLFW.GLFW_RELEASE)
            keyMap.remove(key)
        else
            keyMap += key

    }

    fun onClicked(button: MouseButton, action: MouseAction): Boolean = fOnClicked(button, action)

    fun update() {
        keyMapPrev.clear()
        keyMapPrev.addAll(keyMap)
    }

    fun isKeyPressed(key: Int): Boolean {
        return key !in keyMapPrev && key in keyMap
    }

    fun isKeyRepeated(key: Int): Boolean {
        return key in keyMapPrev && key in keyMap
    }

    fun isKeyDown(key: Int): Boolean {
        return key in keyMap
    }

    fun isKeyGetReleased(key: Int): Boolean {
        return key in keyMapPrev && key !in keyMap
    }

    fun isKeyReleased(key: Int): Boolean {
        return key !in keyMap
    }

    fun resetInput() {
        keyMapPrev.clear()
        keyMap.clear()
    }

}