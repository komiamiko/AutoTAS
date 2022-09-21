import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.stage.Stage

class Main : Application() {
    var scene = Scene(HBox(), 1600.0, 900.0)

    override fun start(stage: Stage) {
        // set up UI
        stage.minWidth = 400.0
        stage.minHeight = 225.0
        stage.title = "AutoTAS prototype"
        stage.scene = scene
        stage.show()
    }
}