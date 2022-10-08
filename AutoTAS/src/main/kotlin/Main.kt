import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.stage.Stage
import kotlin.concurrent.thread

class Main : Application() {
    var hb1 = Pane()
    var scene = Scene(hb1, 1600.0, 900.0)

    override fun start(stage: Stage) {
        // set up test stage
        var state = State(0, 512, 512 * 3, 0, 0,
            World(512, 512 * 3, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()))
        state.world.surfacesUp.add(Surface(0, 1024 * 10, 0, false))
        state.world.surfacesUp.add(Surface(1024 * 10, 1024 * 20, 1024, false))
        state.world.surfacesLeft.add(Surface(0, 1024, 1024 * 10, false))
        var gamePanel = GamePanel(state)
        hb1.children.add(gamePanel)
        // set up UI
        stage.minWidth = 400.0
        stage.minHeight = 225.0
        stage.title = "AutoTAS prototype"
        stage.scene = scene
        stage.show()
        // key listener
        gamePanel.onKeyPressed = EventHandler {
            if(it.code == KeyCode.A) {
                gamePanel.manualInput.left = true;
            }else if(it.code == KeyCode.D) {
                gamePanel.manualInput.right = true;
            }else if(it.code == KeyCode.J) {
                gamePanel.manualInput.jump = true;
            }
        }
        gamePanel.onKeyReleased = EventHandler {
            if(it.code == KeyCode.A) {
                gamePanel.manualInput.left = false;
            }else if(it.code == KeyCode.D) {
                gamePanel.manualInput.right = false;
            }else if(it.code == KeyCode.J) {
                gamePanel.manualInput.jump = false;
            }
        }
        gamePanel.requestFocus()
        // begin ticking
        thread{
            while(!stage.isShowing) {
                Thread.sleep(33)
            }
            while(stage.isShowing) {
                Thread.sleep(33)
                gamePanel.width = stage.width
                gamePanel.height = stage.height
                gamePanel.tick()
            }
        };
    }
}