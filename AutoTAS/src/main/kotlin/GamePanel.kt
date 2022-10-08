import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import kotlin.math.hypot
import kotlin.math.round
import kotlin.math.roundToInt

class GamePanel(var state: State) : Canvas() {

    var cameraX: Int = 1024 * 15
    var cameraY: Int = 1024 * 2
    var cameraZoom: Int = 50000
    var manualInput = Input(false, false, false)
    var timesDied: Int = 0
    var waypoint: Waypoint? = null
    var saveState: State? = null

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
        // show waypoint
        var waypoint = waypoint;
        if(waypoint != null) {
            gc.stroke = Color.GREEN
            gc.strokeLine(
                (waypoint.x - State.playerRadius) * xmul + xadd,
                waypoint.y * ymul + yadd,
                (waypoint.x + State.playerRadius) * xmul + xadd,
                waypoint.y * ymul + yadd)
            gc.strokeLine(waypoint.x * xmul + xadd,
                (waypoint.y - State.playerRadius) * ymul + yadd,
                waypoint.x * xmul + xadd,
                (waypoint.y + State.playerRadius) * ymul + yadd)
            gc.strokeOval(
                (waypoint.x - State.playerRadius) * xmul + xadd,
                (waypoint.y + State.playerRadius) * ymul + yadd,
                State.playerRadius * 2 * xmul,
                -State.playerRadius * 2 * ymul)
        }
        // frame counter
        gc.fillText(state.frame.toString(), 10.0, 10.0)
        gc.fillText("$timesDied deaths", 10.0, 40.0)
    }

    fun setWaypointByPixel(x: Double, y: Double) {
        val cwidth = width
        val cheight = height
        // figure out camera transform
        val xmul = hypot(cwidth, cheight) / cameraZoom
        val ymul = -xmul
        val xadd = cwidth * 0.5 - cameraX * xmul
        val yadd = cheight * 0.5 - cameraY * ymul
        // create waypoint
        waypoint = Waypoint(((x - xadd) / xmul).roundToInt(), ((y - yadd) / ymul).roundToInt())
    }

    fun getInput(): Input? {
        if(waypoint == null) {
            return manualInput
        }
        return null
    }

    fun tick() {
        if(saveState == null) {
            saveState = state
        }
        var input = getInput()
        if(input != null) {
            state = state.copy()
            val died = state.tickInPlace(input)
            val restore = saveState
            if(died && restore != null) {
                timesDied++
                state = restore
            }
        }
        paint()
    }

}