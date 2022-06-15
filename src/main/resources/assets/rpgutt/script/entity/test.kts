
var result: Any? = Unit

if(situation == "serif") result = Serif(
    atom(grayscale(0.9f), perChar(10), "こんにちは", "これはKotlinScriptで", "書かれているんだ。"),
    atom(grayscale(0.9f), perChar(10), "描画関係は後で", "ちゃんと実装するから"),
    atom(color("pink"), perChar(10), "安心してね")
)

if(situation == "update") {
    if(!isTalking && self.motion == Vec2i.ZERO && Random.nextInt(240) == 0) {
        self.motion += Vec2i(Random.nextInt(20) - 10, Random.nextInt(20) - 10)
    }
}

result
