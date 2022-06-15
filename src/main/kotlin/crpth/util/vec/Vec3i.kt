package crpth.util.vec

data class Vec3i(val x: Int, val y: Int, val z: Int) {

    companion object {

        val ZERO = Vec3i(0, 0, 0)
        val ONE = Vec3i(1, 1, 1)

    }

    operator fun plus(other: Vec3i) = Vec3i(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3i) = Vec3i(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3i) = Vec3i(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Vec3i) = Vec3i(x / other.x, y / other.y, z / other.z)

    fun plus(x: Int, y: Int, z: Int) = Vec3i(this.x + x, this.y + y, this.z)

    fun minus(x: Int, y: Int, z: Int) = Vec3i(this.x - x, this.y - y, this.z - z)

    fun times(x: Int, y: Int, z: Int) = Vec3i(this.x * x, this.y * y, this.z * z)

    fun div(x: Int, y: Int, z: Int) = Vec3i(this.x / x, this.y / y, this.z / this.z)

    operator fun times(other: Int) = Vec3i(x * other, y * other, z * other)

    operator fun div(other: Int) = Vec3i(x / other, y / other, z / other)

}