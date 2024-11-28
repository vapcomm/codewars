package online.vapcom.codewars.numbers

/**
 * #XX Count ones in a segment - 4 kyu
 *
 * https://www.codewars.com/kata/596d34df24a04ee1e3000a25/train/kotlin
 *
 * @return sum of all '1' occurrences in binary representations of numbers
 * between 'left' and 'right' (including both) or 0 if left or right negative
 */
fun countOnes(left: Long, right: Long): Long {
    if (left < 0 || right < 0 || left > right)
        return 0

    return countBits(right) - countBits(left)
}

internal val hexBits = intArrayOf(
    // 1  2  3  4  5  6  7  8  9  A  B  C  D  E  F
    0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4
)

internal val bitsSum = intArrayOf(
    // 1  2  3  4  5  6   7   8   9   A   B   C   D   E   F
    0, 1, 2, 4, 5, 7, 9, 12, 13, 15, 17, 20, 22, 25, 28, 32
)


internal fun countBits(value: Long): Long {
    println("\n== value: 0x${"%02X".format(value)}")

    var bits = 0L

    var i = 15
    do {
        val tetrad = ((value shr (i * 4)) and 0x0FL).toInt()
        println("-- i: $i, quad: 0x${"%02X".format(tetrad)}")


        if (tetrad != 0) {
            bits += hexBits[tetrad]
            break
        }

        i--
    } while (i >= 0)

    println("-- MIDDLE: i: $i, bits: $bits")

    if (i == 0) { // only first tetrad in value
        val tetrad = (value and 0x0FL).toInt()
        return bitsSum[tetrad].toLong()
    }

    var j = 1
    while (j <= i) {
        val tetrad = ((value shr (j * 4)) and 0x0FL).toInt()
        val b = tetrad * (32 shl (j - 1))
        bits += b
        println("-- j: $j, quad: 0x${"%02X".format(tetrad)}, b: $b, bits: $bits")

        j++
    }

    bits += bitsSum[(value and 0x0FL).toInt()] // first tetrad bits


    /*
        for (i in 0 until (64/4)) {
            val tetrad = ((value shr (i * 4)) and 0x0FL).toInt()
            val b = bitsSum[tetrad] * (i + 1) //???
            bits += b
            println("-- i: $i, quad: 0x${"%02X".format(tetrad)}, b: $b, bits: $bits")
        }
    */
    return bits
}
