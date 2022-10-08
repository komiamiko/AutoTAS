class State(
    var frame: Int,
    var playerX: Int,
    var playerY: Int,
    var playerVX: Int,
    var playerVY: Int,
    var world: World,
    var grounded: Boolean = false
) {

    companion object {
        val tileSize: Int = 1024
        val playerRadius: Int = 448
        val hvMove: Int = 39
        val hvSoftcap: Int = 155
        val hvLinearDrag: Int = 9
        val gravity: Int = 16
        val jump: Int = 308
    }

    fun copy(): State {
        return State(frame, playerX, playerY, playerVX, playerVY, world, grounded)
    }

    fun tickInPlace(input: Input): Boolean {
        // increment frame counter
        frame++
        // gravity
        playerVY -= gravity
        // jumping
        if(grounded && input.jump) {
            playerVY = jump
        }
        // horizontal drag
        val isLeft = input.left && !input.right
        val isRight = input.right && !input.left
        playerVX = kotlin.math.max(kotlin.math.min((if(isRight)hvSoftcap else 0), playerVX), playerVX - hvLinearDrag)
        playerVX = kotlin.math.min(kotlin.math.max((if(isLeft)-hvSoftcap else 0), playerVX), playerVX + hvLinearDrag)
        // horizontal acceleration
        if(isLeft) {
            playerVX = kotlin.math.min(playerVX, kotlin.math.max(playerVX - hvMove, -hvSoftcap))
        }
        if(isRight) {
            playerVX = kotlin.math.max(playerVX, kotlin.math.min(playerVX + hvMove, hvSoftcap))
        }
        // movement
        var targetX = playerX + playerVX
        if(playerVX <= 0) {
            // moving left
            for(surface in world.surfacesRight) {
                if(playerY + playerRadius > surface.x1
                    && playerY - playerRadius < surface.x2
                    && playerX - playerRadius >= surface.y
                    && targetX - playerRadius <= surface.y) {
                    if(surface.spike) {
                        return true
                    }
                    targetX = surface.y + playerRadius
                    playerVX = 0
                }
            }
        }
        if(playerVX >= 0) {
            // moving right
            for(surface in world.surfacesLeft) {
                if(playerY + playerRadius > surface.x1
                    && playerY - playerRadius < surface.x2
                    && playerX + playerRadius <= surface.y
                    && targetX + playerRadius >= surface.y) {
                    if(surface.spike) {
                        return true
                    }
                    targetX = surface.y - playerRadius
                    playerVX = 0
                }
            }
        }
        grounded = false
        playerX = targetX
        var targetY = playerY + playerVY
        if(playerVY <= 0) {
            // moving down
            for(surface in world.surfacesUp) {
                if(playerX + playerRadius > surface.x1
                    && playerX - playerRadius < surface.x2
                    && playerY - playerRadius >= surface.y
                    && targetY - playerRadius <= surface.y) {
                    if(surface.spike) {
                        return true
                    }
                    targetY = surface.y + playerRadius
                    playerVY = 0
                    grounded = true
                }
            }
        }
        if(playerVY >= 0) {
            // moving up
            for(surface in world.surfacesDown) {
                if(playerX + playerRadius > surface.x1
                    && playerX - playerRadius < surface.x2
                    && playerY + playerRadius <= surface.y
                    && targetY + playerRadius >= surface.y) {
                    if(surface.spike) {
                        return true
                    }
                    targetY = surface.y - playerRadius
                    playerVY = 0
                }
            }
        }
        playerY = targetY
        return false
    }

}