package online.vapcom.codewars.strings

/**
 * RoboScript #1 - Implement Syntax Highlighting
 *
 * https://www.codewars.com/kata/58708934a44cfccca60000c4/train/kotlin
 * "F3RF5LF7"
 * "FFFR345F2LL"
 * "8(76)RL8F8))92LFR)60193(0887R53(57R"
 * "RRRRRF45L3F2"
 * "RRRRR(F45L3)F2"
 */
fun highlight(code: String): String {
    val regex = Regex("(F+)|(R+)|(L+)|(\\d+)|(\\(+)|(\\)+)")
    return regex.findAll(code).map { mr ->
        val color = when(val char = mr.value.first()) {
            'F' -> "pink"
            'L' -> "red"
            'R' -> "green"
            else -> if (char.isDigit()) "orange" else ""
        }
        if (color.isNotEmpty())
            "<span style=\"color: $color\">${mr.value}</span>"
        else mr.value
    }.joinToString("")
}

/**
 * RoboScript #2 - Implement the RS1 Specification
 *
 * https://www.codewars.com/kata/5870fa11aa0428da750000da/train/kotlin
 */
fun execute(code: String): String {

    fun compileCode(code: String) : String {
        if (code.isBlank()) return ""

        val elementsRegex = Regex("(F+)|(R+)|(L+)|(\\d+)")
        val elements = elementsRegex.findAll(code).toList()

        val sb = StringBuilder()
        var i = 0
        while (i < elements.size) {
            // println("-- elements[$i]: '${elements[i].value}'")
            if (elements[i].value.first().isDigit()) {
                if (i > 0) {
                    val ch = elements[i-1].value.last()
                    val num = elements[i].value.toIntOrNull() ?: 1
                    repeat(num - 1 ) { sb.append(ch) }
                } // else should be syntax error exception due number at code's first element
            } else sb.append(elements[i].value)

            i++
        }

        return sb.toString()
    }

    fun expandEast(matrix: Array<ByteArray>): Array<ByteArray> {
        for (y in matrix.indices) {
            matrix[y] = matrix[y].plus(0)
        }
        return matrix
    }

    fun expandWest(matrix: Array<ByteArray>): Array<ByteArray> {
        val newSize = matrix[0].size + 1
        for (y in matrix.indices) {
            val row = ByteArray(newSize)
            matrix[y].copyInto(row, 1)
            matrix[y] = row
        }
        return matrix
    }

    fun expandNorth(matrix: Array<ByteArray>): Array<ByteArray> {
        val rowSize = matrix[0].size
        val column = Array<ByteArray>(matrix.size + 1) { y ->
            if (y == 0) ByteArray(rowSize)
            else matrix[y - 1]
        }

        return column
    }

    fun expandSouth(matrix: Array<ByteArray>): Array<ByteArray> {
        return matrix.plus(ByteArray(matrix[0].size))
    }

    fun matrixToString(matrix: Array<ByteArray>) : String {
        return matrix.joinToString("\r\n") { row ->
            row.joinToString ("") { cell ->
                when(cell) {
                    0.toByte() -> " "
                    1.toByte() -> "*"
                    else -> "?"
                }
            }
        }
    }

    // println("-- code: '$code'")
    val commands = compileCode(code)
    // println("-- commands: '$commands'")

    var theMatrix = Array<ByteArray>(1) { ByteArray(1) { 1 } }
    var heading = 90        // azimuth of a next step: 0 - north, 90 - east (start direction), 180 - south, 270 - west
    var x = 0               // current robot's coordinates, it always starts at zero matrix cell
    var y = 0
    commands.forEach { cmd ->
        // println("-- run: 1: '$cmd', $x,$y -> $heading, matrix: [${theMatrix[0].size}x${theMatrix.size}]")
        when(cmd) {
            'L' -> { // turn left
                heading -= 90
                if (heading < 0) heading += 360
            }
            'R' -> { // turn right
                heading += 90
                if (heading >= 360) heading -= 360
            }
            'F' -> { // move forward
                // calculate deltas
                val dx = when (heading) {
                    90 -> 1
                    270 -> -1
                    else -> 0
                }
                val dy = when (heading) {
                    0 -> -1
                    180 -> 1
                    else -> 0
                }

                // check matrix expansion needs
                when {
                    x + dx >= theMatrix[y].size -> theMatrix = expandEast(theMatrix)
                    x + dx < 0 -> {
                        theMatrix = expandWest(theMatrix)
                        x++
                    }
                }
                when {
                    y + dy >= theMatrix.size -> theMatrix = expandSouth(theMatrix)
                    y + dy < 0 -> {
                        theMatrix = expandNorth(theMatrix)
                        y++
                    }
                }

                // println("-- run: 2: '$cmd', $x,$y, dx:$dx, dy:$dy, => ${x+dx},${y+dy}, matrix: [${theMatrix[0].size}x${theMatrix.size}]")

                x += dx
                y += dy

                // println("-- run: 3: '$cmd' => $x,$y")

                theMatrix[y][x] = 1.toByte()
            } // F
        } // when cmd
    }

    val result = matrixToString(theMatrix)
    // println("-- result:\n$result")
    return result
}

