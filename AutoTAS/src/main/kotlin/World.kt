data class World(
    var spawnX: Int,
    var spawnY: Int,
    var surfacesLeft: MutableList<Surface>,
    var surfacesRight: MutableList<Surface>,
    var surfacesUp: MutableList<Surface>,
    var surfacesDown: MutableList<Surface>
)