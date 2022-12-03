
object : IEntityAI {
    
    override val updateType: UpdateType = UpdateType.WHEN_RENDERED_WIDER

    override fun getSerif(params: EntityParams) = Serif(
        atom(grayscale(0.9f), perChar(10), "こんにちは", "これはKotlinScriptで", "書かれているんだ。"),
        atom(grayscale(0.9f), perChar(10), "描画関係は後で", "ちゃんと実装するから"),
        atom(color(1.0, 0.0, 0.2), perChar(10), "安心してね")
    )

    override fun update(params: EntityParams) {

        params.self.speed = 1

        if(!params.isTalking && params.self.motion == Vec2i.ZERO) {
            params.self.turnRight()
            params.self.move(amount = Random.nextInt(50) + 50)
        }

    }

}
