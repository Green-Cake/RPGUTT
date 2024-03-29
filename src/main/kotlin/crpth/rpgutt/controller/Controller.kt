package crpth.rpgutt.controller

import crpth.rpgutt.RpgUtt
import crpth.util.type.Direction
import org.lwjgl.glfw.GLFW

class ControllerManager : Controller {

    override var isEnabled = false

    val keyboard = ControllerKeyboard()
    val hat = ControllerHat()
    val joystick = ControllerJoystick()

    override val directions get() = joystick.directions + keyboard.directions

    override fun update() {

        if(!isEnabled)
            return

        keyboard.enable()
        hat.enable()
        joystick.enable()

        joystick.update()
        hat.update()
        keyboard.update()

    }

}

interface Controller {

    var isEnabled: Boolean

    val directions: Set<Direction>

    fun update()

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

}

class ControllerKeyboard : Controller {

    override val directions = mutableSetOf<Direction>()

    override var isEnabled = false

    override fun update() {

        directions.clear()

        if(!isEnabled)
            return

        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_UP) || RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_W))
            directions += Direction.NORTH

        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_RIGHT) || RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_D))
            directions += Direction.EAST

        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_DOWN) || RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_S))
            directions += Direction.SOUTH

        if(RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_LEFT) || RpgUtt.richWindow.isKeyDown(GLFW.GLFW_KEY_A))
            directions += Direction.WEST

    }

}

class ControllerHat(val joystickID: Int = GLFW.GLFW_JOYSTICK_1) : Controller {

    override val directions = mutableSetOf<Direction>()

    override var isEnabled = false

    val isPresent get() = GLFW.glfwJoystickPresent(joystickID)

    var hat_n = 0

    override fun update() {

        directions.clear()

        if(!isEnabled)
            return

        if(!isPresent)
            return

        val hats = GLFW.glfwGetJoystickHats(joystickID)

        val hat = hats?.get(hat_n)?.toInt() ?: return

        if(hat and GLFW.GLFW_HAT_UP != 0)
            directions += Direction.NORTH

        if(hat and GLFW.GLFW_HAT_RIGHT != 0)
            directions += Direction.EAST

        if(hat and GLFW.GLFW_HAT_DOWN != 0)
            directions += Direction.SOUTH

        if(hat and GLFW.GLFW_HAT_LEFT != 0)
            directions += Direction.WEST

    }

}

class ControllerJoystick(val joystickID: Int = GLFW.GLFW_JOYSTICK_1) : Controller {

    override val directions = mutableSetOf<Direction>()

    override var isEnabled = false

    val isPresent get() = GLFW.glfwJoystickPresent(joystickID)

    override fun update() {

        directions.clear()

        if(!isEnabled)
            return

        if(!isPresent)
            return

        val axes = GLFW.glfwGetJoystickAxes(joystickID) ?: return

        if(axes[1] < -0.5f)
            directions += Direction.NORTH

        if(axes[0] > 0.5f)
            directions += Direction.EAST

        if(axes[1] > 0.5f)
            directions += Direction.SOUTH

        if(axes[0] < -0.5f)
            directions += Direction.WEST

    }

}