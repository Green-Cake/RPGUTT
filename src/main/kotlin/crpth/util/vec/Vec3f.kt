package crpth.util.vec

data class Vec3f(val x: Float, val y: Float, val z: Float) {

    companion object {

        val ZERO = Vec3f(0.0f, 0.0f, 0.0f)
        val ONE = Vec3f(1.0f, 1.0f, 1.0f)

    }

    operator fun plus(other: Vec3f) = Vec3f(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3f) = Vec3f(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3f) = Vec3f(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Vec3f) = Vec3f(x / other.x, y / other.y, z / other.z)

    fun plus(x: Float, y: Float, z: Float) = Vec3f(this.x + x, this.y + y, this.z)

    fun minus(x: Float, y: Float, z: Float) = Vec3f(this.x - x, this.y - y, this.z - z)

    fun times(x: Float, y: Float, z: Float) = Vec3f(this.x * x, this.y * y, this.z * z)

    fun div(x: Float, y: Float, z: Float) = Vec3f(this.x / x, this.y / y, this.z / this.z)

    operator fun times(other: Float) = Vec3f(x * other, y * other, z * other)

    operator fun div(other: Float) = Vec3f(x / other, y / other, z / other)

    operator fun unaryMinus() = times(-1f)

    fun max(): Float = kotlin.math.max(x, kotlin.math.max(y, z))

    fun min(): Float = kotlin.math.min(x, kotlin.math.min(y, z))

    fun abs(): Vec3f = Vec3f(kotlin.math.abs(x), kotlin.math.abs(y), kotlin.math.abs(z))

}