package online.vapcom.codewars.algorithms

object Spiralizor {

    /**
     * #29 Make a spiral - 3 kyu
     *
     * https://www.codewars.com/kata/534e01fbbb17187c7e0000c6
     *
     */
    fun spiralize(n: Int): Array<ByteArray> {
        val field = Array(n) { ByteArray(n) }

        class Head(var x: Int, var y: Int, var direction: Char) {
            fun canMove(): Boolean {
                return when (direction) {
                    'U' -> {
//                        000
//                        0x0
//                        010
                        y - 2 >= 0
                        && field[y - 1][x] == 0.toByte()
                        && field[y - 2][x] == 0.toByte()
                        && (if (x > 0) field[y - 1][x - 1] == 0.toByte() && field[y - 2][x - 1] == 0.toByte() else true)
                        && (if (x < n - 1) field[y - 1][x + 1] == 0.toByte() && field[y - 2][x + 1] == 0.toByte() else true)
                    }

                    'D' -> {
//                        010
//                        0x0
//                        000
                        y + 2 < n && field[y + 1][x] == 0.toByte() && field[y + 2][x] == 0.toByte()
                        && (if (x > 0) field[y + 1][x - 1] == 0.toByte() && field[y + 2][x - 1] == 0.toByte() else true)
                        && (if (x < n - 1) field[y + 1][x + 1] == 0.toByte() && field[y + 2][x + 1] == 0.toByte() else true)
                    }
                    'L' -> {
//                        000
//                        0x1
//                        000
                        x - 2 >= 0 && field[y][x - 1] == 0.toByte() && field[y][x - 2] == 0.toByte()
                        && (if (y > 0) field[y - 1][x - 1] == 0.toByte() && field[y - 1][x - 2] == 0.toByte() else true)
                        && (if (y < n - 1) field[y + 1][x - 1] == 0.toByte() && field[y + 1][x - 2] == 0.toByte() else true)
                    }
                    'R' -> {
//                        000
//                        1x0
//                        000
                        x + 2 < n && field[y][x + 1] == 0.toByte() && field[y][x + 2] == 0.toByte()
                        && (if (y > 0) field[y - 1][x + 1] == 0.toByte() && field[y - 1][x + 2] == 0.toByte() else true)
                        && (if (y < n - 1) field[y + 1][x + 1] == 0.toByte() && field[y + 1][x + 2] == 0.toByte() else true)
                    }
                    else -> false
                }
            }

            fun move() {
                when (direction) {
                    'U' -> y--
                    'D' -> y++
                    'L' -> x--
                    'R' -> x++
                }
                field[y][x] = 1
            }

            /**
             * Turn direction clockwise
             */
            fun turn() {
                direction = when (direction) {
                    'U' -> 'R'
                    'D' -> 'L'
                    'L' -> 'U'
                    'R' -> 'D'
                    else -> direction
                }
            }
        }


        // fast fill of outer sides
        field[0].fill(1)                // top
        for (i in 1..n - 2) field[i][n - 1] = 1    // right side
        field[field.lastIndex].fill(1)  // bottom

        val head = Head(0, n - 1, 'U')

        do {
            while (head.canMove()) {
                head.move()
            }
            head.turn()
        } while (head.canMove())

        println(field.joinToString("\n") { it.joinToString() })

        return field
    }

}
