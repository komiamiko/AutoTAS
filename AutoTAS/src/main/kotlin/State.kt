class State(
    var frame: Int,
    var playerX: Int,
    var playerY: Int,
    var playerVX: Int,
    var playerVY: Int,
    var world: World,
) {

    fun copy() {
        State(frame, playerX, playerY, playerVX, playerVY, world)
    }

    fun tickInPlace(input: Input) {
        State(frame+1, playerX+playerVX, playerY+playerVY, playerVX, playerVY, world)
    }

}