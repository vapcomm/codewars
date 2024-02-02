package online.vapcom.codewars.algorithms

import kotlin.test.assertEquals
import kotlin.test.Test

class PlantsAndZombiesTest {

    @Test
    fun runExamples() = exampleSols.zip(exampleTests).forEach { (sol, tst) -> assertEquals(sol, plantsAndZombies(tst.first, tst.second)) }

    private val exampleSols = listOf(10, 12, 20, 19, null)

    private val exampleTests = listOf(
        Pair(
            arrayOf(
                "2       ",
                "  S     ",
                "21  S   ",
                "13      ",
                "2 3     "
            ),
            arrayOf(
                intArrayOf(0, 4, 28),
                intArrayOf(1, 1, 6),
                intArrayOf(2, 0, 10),
                intArrayOf(2, 4, 15),
                intArrayOf(3, 2, 16),
                intArrayOf(3, 3, 13)
            )
        ),
        Pair(
            arrayOf(
                "11      ",
                " 2S     ",
                "11S     ",
                "3       ",
                "13      "
            ),
            arrayOf(
                intArrayOf(0, 3, 16),
                intArrayOf(2, 2, 15),
                intArrayOf(2, 1, 16),
                intArrayOf(4, 4, 30),
                intArrayOf(4, 2, 12),
                intArrayOf(5, 0, 14),
                intArrayOf(7, 3, 16),
                intArrayOf(7, 0, 13)
            )
        ),
        Pair(
            arrayOf(
                "12        ",
                "3S        ",
                "2S        ",
                "1S        ",
                "2         ",
                "3         "
            ),
            arrayOf(
                intArrayOf(0, 0, 18),
                intArrayOf(2, 3, 12),
                intArrayOf(2, 5, 25),
                intArrayOf(4, 2, 21),
                intArrayOf(6, 1, 35),
                intArrayOf(6, 4, 9),
                intArrayOf(8, 0, 22),
                intArrayOf(8, 1, 8),
                intArrayOf(8, 2, 17),
                intArrayOf(10, 3, 18),
                intArrayOf(11, 0, 15),
                intArrayOf(12, 4, 21)
            )
        ),
        Pair(
            arrayOf(
                "12      ",
                "2S      ",
                "1S      ",
                "2S      ",
                "3       "
            ),
            arrayOf(
                intArrayOf(0, 0, 15),
                intArrayOf(1, 1, 18),
                intArrayOf(2, 2, 14),
                intArrayOf(3, 3, 15),
                intArrayOf(4, 4, 13),
                intArrayOf(5, 0, 12),
                intArrayOf(6, 1, 19),
                intArrayOf(7, 2, 11),
                intArrayOf(8, 3, 17),
                intArrayOf(9, 4, 18),
                intArrayOf(10, 0, 15),
                intArrayOf(11, 4, 14)
            )
        ),
        Pair(
            arrayOf(
                "1         ",
                "SS        ",
                "SSS       ",
                "SSS       ",
                "SS        ",
                "1         "
            ),
            arrayOf(
                intArrayOf(0, 2, 16),
                intArrayOf(1, 3, 19),
                intArrayOf(2, 0, 18),
                intArrayOf(4, 2, 21),
                intArrayOf(6, 3, 20),
                intArrayOf(7, 5, 17),
                intArrayOf(8, 1, 21),
                intArrayOf(8, 2, 11),
                intArrayOf(9, 0, 10),
                intArrayOf(11, 4, 23),
                intArrayOf(12, 1, 15),
                intArrayOf(13, 3, 22)
            )
        )
    )

    @Test
    fun attemptSmallerLawns1() {
        val lawn = arrayOf(
            "24        ",
            "  31      ",
            "21SS      ",
            "2S        ",
            "1S1 1     ",
            "1S2S      ",
            "4 S       ",
            "5         "
        )
        val zombies = arrayOf(
            intArrayOf(1, 1, 18), intArrayOf(1, 2, 22), intArrayOf(1, 4, 18),
            intArrayOf(1, 5, 22), intArrayOf(1, 6, 22), intArrayOf(1, 7, 22),
            intArrayOf(2, 0, 29), intArrayOf(2, 1, 11), intArrayOf(2, 2, 14),
            intArrayOf(2, 3, 14), intArrayOf(2, 5, 14), intArrayOf(2, 7, 14),
            intArrayOf(3, 3, 9), intArrayOf(3, 6, 17), intArrayOf(4, 0, 22),
            intArrayOf(4, 1, 10), intArrayOf(4, 5, 13), intArrayOf(4, 6, 11),
            intArrayOf(4, 7, 13), intArrayOf(6, 0, 14), intArrayOf(6, 2, 15),
            intArrayOf(8, 3, 9), intArrayOf(8, 4, 18), intArrayOf(8, 5, 11),
            intArrayOf(8, 7, 11), intArrayOf(10, 0, 13), intArrayOf(10, 1, 11),
            intArrayOf(10, 2, 13), intArrayOf(10, 3, 7), intArrayOf(10, 4, 12),
            intArrayOf(11, 6, 15))

        assertEquals(null, plantsAndZombies(lawn, zombies))
    }

}
