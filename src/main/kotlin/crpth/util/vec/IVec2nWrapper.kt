package crpth.util.vec

interface IVec2nWrapper<T : Number> : IVec2n<T> {

    val value: IVec2n<T>

    override val x: T get() = value.x

    override val y: T get() = value.y

    override fun setVertex() = value.setVertex()

}