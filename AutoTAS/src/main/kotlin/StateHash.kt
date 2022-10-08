data class StateHash(val u: Long, val v: Long) {
    fun append(input: Input): StateHash {
        var xu = u
        var xv = v
        xu = xu + 528131773
        xu = xu xor if(input.left)1 else 0
        xu = xu xor if(input.right)2 else 0
        xu = xu xor if(input.jump)4 else 0
        xu = xu xor xu.shl(17)
        xu = xu xor xu.shr(22)
        xv = xv xor xu
        xv *= 868506519
        xu = xu + xv
        return StateHash(xu, xv)
    }
}
