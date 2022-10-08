import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlin.math.hypot

class GamePanel(var state: State) : Canvas() {

    var cameraX: Int = 1024 * 15
    var cameraY: Int = 1024 * 2
    var cameraZoom: Int = 50000
    var manualInput = Input(false, false, false)
    var timesDied: Int = 0

    fun paint() {
        // get drawing context
        val gc = graphicsContext2D
        // figure out the drawing region
        val cwidth = width
        val cheight = height
        // clear with background
        gc.fill = Color.WHITE
        gc.fillRect(0.0, 0.0, cwidth, cheight)
        // figure out camera transform
        val xmul = hypot(cwidth, cheight) / cameraZoom
        val ymul = -xmul
        val xadd = cwidth * 0.5 - cameraX * xmul
        val yadd = cheight * 0.5 - cameraY * ymul
        // show surfaces
        gc.lineWidth = 3.0
        for(surface in state.world.surfacesUp) {
            gc.stroke = if(surface.spike) Color.RED else Color.BLACK
            val y = surface.y * ymul + yadd
            gc.strokeLine(surface.x1 * xmul + xadd, y, surface.x2 * xmul + xadd, y)
        }
        for(surface in state.world.surfacesDown) {
            gc.stroke = if(surface.spike) Color.RED else Color.BLACK
            val y = surface.y * ymul + yadd
            gc.strokeLine(surface.x1 * xmul + xadd, y, surface.x2 * xmul + xadd, y)
        }
        for(surface in state.world.surfacesLeft) {
            gc.stroke = if(surface.spike) Color.RED else Color.BLACK
            val x = surface.y * xmul + xadd
            gc.strokeLine(x, surface.x1 * ymul + yadd, x, surface.x2 * ymul + yadd)
        }
        for(surface in state.world.surfacesRight) {
            gc.stroke = if(surface.spike) Color.RED else Color.BLACK
            val x = surface.y * xmul + xadd
            gc.strokeLine(x, surface.x1 * ymul + yadd, x, surface.x2 * ymul + yadd)
        }
        // show player
        gc.fill = Color.BLUE
        gc.fillRect(
            (state.playerX - State.playerRadius) * xmul + xadd,
            (state.playerY + State.playerRadius) * ymul + yadd,
            State.playerRadius * 2 * xmul,
            -State.playerRadius * 2 * ymul)
        // frame counter
        gc.fillText(state.frame.toString(), 10.0, 10.0)
        gc.fillText("$timesDied deaths", 10.0, 40.0)
    }

    fun getInput(): Input {
        return manualInput
    }

    fun tick() {
        state = state.copy()
        val died = state.tickInPlace(getInput())
        if(died) {
            timesDied++
            state.playerX = state.world.spawnX
            state.playerY = state.world.spawnY
        }
        paint()
    }

}