package crpth.util.vec

data class Vec3ui(val x: UInt, val y: UInt, val z: UInt) {

    companion object {

        val ZERO = Vec3ui(0u, 0u, 0u)
        val ONE = Vec3ui(1u, 1u, 1u)

    }

    inline fun map(transform: (UInt)->UInt) = Vec3ui(transform(x), transform(y), transform(z))

    operator fun plus(other: Vec3ui) = Vec3ui(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vec3ui) = Vec3ui(x - other.x, y - other.y, z - other.z)

    operator fun times(other: Vec3ui) = Vec3ui(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Vec3ui) = Vec3ui(x / other.x, y / other.y, z / other.z)

    fun plus(x: UInt, y: UInt, z: UInt) = Vec3ui(this.x + x, this.y + y, this.z)

    fun minus(x: UInt, y: UInt, z: UInt) = Vec3ui(this.x - x, this.y - y, this.z - z)

    fun times(x: UInt, y: UInt, z: UInt) = Vec3ui(this.x * x, this.y * y, this.z * z)

    fun div(x: UInt, y: UInt, z: UInt) = Vec3ui(this.x / x, this.y / y, this.z / this.z)

    operator fun times(other: UInt) = Vec3ui(x * other, y * other, z * other)

    operator fun div(other: UInt) = Vec3ui(x / other, y / other, z / other)

}