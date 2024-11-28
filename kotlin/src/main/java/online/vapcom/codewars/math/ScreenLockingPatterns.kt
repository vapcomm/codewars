package online.vapcom.codewars.math

/*
 * A B C    0 1 2
 * D E F -> 3 4 5
 * G H I    6 7 8
 */
private val DIRECT_POINTS = arrayOf(
    intArrayOf(1, 5, 4, 7, 3),          // 0
    intArrayOf(2, 5, 8, 4, 6, 3, 0),    // 1
    intArrayOf(5, 7, 4, 3, 1),          // 2
    intArrayOf(0, 1, 2, 4, 8, 7, 6),    // 3
    intArrayOf(1, 2, 5, 8, 7, 6, 3, 0), // 4
    intArrayOf(2, 8, 7, 6, 4, 0, 1),    // 5
    intArrayOf(3, 1, 4, 5, 7),          // 6
    intArrayOf(4, 2, 5, 8, 6, 3, 0),    // 7
    intArrayOf(5, 7, 3, 4, 1),          // 8
)

private val INDIRECT_POINTS = arrayOf(
    // used -> indirect point
    arrayOf(Pair(1, 2), Pair(4, 8), Pair(3, 6)), // 0
    arrayOf(Pair(4, 7)),                         // 1
    arrayOf(Pair(5, 8), Pair(4, 6), Pair(1, 0)), // 2
    arrayOf(Pair(4, 5)),                         // 3
    arrayOf(),                                   // 4
    arrayOf(Pair(4, 3)),                         // 5
    arrayOf(Pair(3, 0), Pair(4, 2), Pair(7, 8)), // 6
    arrayOf(Pair(4, 1)),                         // 7
    arrayOf(Pair(5, 2), Pair(7, 6), Pair(4, 0))  // 8
)

private class UsedPoints(
    val used: BooleanArray = BooleanArray(9),
    var usedNumber: Int = 0
) {
    fun use(point: Int) {
        used[point] = true
        usedNumber++
    }

    fun isAllUsed(): Boolean = usedNumber >= used.size
    fun isNotUsed(point: Int): Boolean = !used[point]

    fun newUsedPoints(point: Int): UsedPoints {
        val newUsed = UsedPoints(used.clone(), usedNumber)
        newUsed.use(point)
        return newUsed
    }

    override fun toString(): String {
        val result = mutableListOf<Int>()
        used.forEachIndexed { i, b ->
            if (b) result.add(i)
        }
        return result.joinToString()
    }
}

/**
 * Screen Locking Patterns
 *
 * https://www.codewars.com/kata/585894545a8a07255e0002f1/train/kotlin
 *
 * @return the number of possible patterns starting from a given first point, that have a given length.
 */
fun countPatternsFrom(firstPoint: String, length: Int): Int {

    if (firstPoint.isBlank() || firstPoint.first() < 'A' || firstPoint.first() > 'I')
        return 0

    if (length < 2)
        return length

    // use letters indexes to simplify work with them
    val first = firstPoint.first().code - 'A'.code
    val startLines = DIRECT_POINTS[first]
    // println("== first: '$first', length: $length, startLines: ${startLines.joinToString { it.toString() }}")

    var waysCount = 0
    val firstUsed = UsedPoints()
    firstUsed.use(first)

    startLines.forEach { next ->
        waysCount += lineTo(next, firstUsed.newUsedPoints(next), length - 1)
    }

    return waysCount
}

private fun lineTo(pointTo: Int, used: UsedPoints, length: Int): Int {
    // println("${"-".repeat(length)} lineTo: '$pointTo', length: $length, used: '$used'")

    if (used.isAllUsed()) {
        // println("${"-".repeat(length)} all used, STOP")
        return if (length <= 1) 1 else 0
    }

    if (length <= 1) {
        // println("${"-".repeat(length)} reached length, STOP")
        return 1
    }

    var ways = 0
    val nextPoints = DIRECT_POINTS[pointTo]
    nextPoints.forEach { next ->
        if(used.isNotUsed(next)) {
            // println("${"-".repeat(length)} direct $pointTo->$next ")
            ways += lineTo(next, used.newUsedPoints(next), length - 1)
        }
        else {
            // println("${"-".repeat(length)} try indirect $pointTo via $next")

            val indirectPoint = INDIRECT_POINTS[pointTo].find { it.first == next }
            if (indirectPoint != null && used.isNotUsed(indirectPoint.second)) {
                // println("${"-".repeat(length)} indirect $pointTo->$next->${indirectPoint.second}")
                ways += lineTo(indirectPoint.second, used.newUsedPoints(indirectPoint.second), length - 1)
            }
        }
    }

    // println("${"-".repeat(length)} pointTo: $pointTo, ways: $ways")

    return ways
}
