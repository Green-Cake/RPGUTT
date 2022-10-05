
//import crpth.rpgutt.script.lib.*

var result: Any? = Unit

if(situation == "serif") result = Serif(
    atom(grayscale(0.9f), perChar(10), "こんにちは", "これはKotlinScriptで", "書かれているんだ。"),
    atom(grayscale(0.9f), perChar(10), "描画関係は後で", "ちゃんと実装するから"),
    atom(color(1.0, 0.0, 0.2), perChar(10), "安心してね")
)

if(situation == "update") {

    if(!isTalking && self.motion == Vec2i.ZERO) {
        self.turnRight()
        self.move(amount = Random.nextInt(50) + 50)
    }
}

result
