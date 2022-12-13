import crpth.util.type.Direction
import crpth.rpgutt.entity.ai.EntityParams
import crpth.rpgutt.entity.ai.IEntityAI
import crpth.rpgutt.entity.ai.UpdateType
import crpth.rpgutt.script.lib.*
import crpth.util.vec.Vec2i
import kotlin.math.abs

object : IEntityAI {
    
    override val updateType: UpdateType = UpdateType.WHEN_RENDERED_WIDER

    override fun getSerif(params: EntityParams) = Serif(
        atom(grayscale(0.9f), perChar(10), "こんにちは", "これはKotlinScriptで", "書かれているんだ。"),
        atom(grayscale(0.9f), perChar(10), "描画関係は後で", "ちゃんと実装するから"),
        atom(color(1.0, 0.0, 0.2), perChar(10), "安心してね")
    )

    override fun init(params: EntityParams) {

        params.self.speed = 16

    }

    override fun update(params: EntityParams) {

        if(!params.isTalking && params.self.motion == Vec2i.ZERO) {

            val a = params.player.pos - params.self.pos

            if(abs(a.x) > abs(a.y)) {

                params.self.direction = if(a.x > 0) Direction.EAST else Direction.WEST

            } else {

                params.self.direction = if(a.y > 0) Direction.NORTH else Direction.SOUTH

            }

            params.self.move(amount = 256)
        }

    }

}
