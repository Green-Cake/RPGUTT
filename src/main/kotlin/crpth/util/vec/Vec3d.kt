package crpth.util.vec

data class Vec3d(val x: Double, val y: Double, val z: Double) {

    companion object {

        val ZERO = Vec3d(0.0, 0.0, 0.0)
        val ONE = Vec3d(1.0, 1.0, 1.0)

    }

    operator fun plus(other: Vec3d) = Vec3d(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3d) = Vec3d(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3d) = Vec3d(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Vec3d) = Vec3d(x / other.x, y / other.y, z / other.z)

    fun plus(x: Double, y: Double, z: Double) = Vec3d(this.x + x, this.y + y, this.z)

    fun minus(x: Double, y: Double, z: Double) = Vec3d(this.x - x, this.y - y, this.z - z)

    fun times(x: Double, y: Double, z: Double) = Vec3d(this.x * x, this.y * y, this.z * z)

    fun div(x: Double, y: Double, z: Double) = Vec3d(this.x / x, this.y / y, this.z / this.z)

    operator fun times(other: Double) = Vec3d(x * other, y * other, z * other)

    operator fun div(other: Double) = Vec3d(x / other, y / other, z / other)

    operator fun unaryMinus() = times(-1.0)

}