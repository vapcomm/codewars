package online.vapcom.codewars.algorithms

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertTrue

class SkyScraperTests4x4 {

    @Test
    fun exampleTest() {
        // example data from https://www.codewars.com/kata/5671d975d81d6c1c87000022
        /*
                1 2
            2 1 4 3
            3 4 1 2   2
       1    4 2 3 1
            1 3 2 4
                3

         */

        val clues = intArrayOf(
            0, 0, 1, 2,
            0, 2, 0, 0,
            0, 3, 0, 0,
            0, 1, 0, 0
        )

        val expected = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun twoPairsCluesTest() {
        /*
              2 1 2
        2   2 1 4 3   2
            3 4 1 2   2
        1   4 2 3 1   3
            1 3 2 4
              2 3

         */

        val clues = intArrayOf(
            0, 2, 1, 2,
            2, 2, 3, 0,
            0, 3, 2, 0,
            0, 1, 0, 2
        )

        val expected = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }


    @Test
    fun bothPairClueTest() {

        /*
   	   	   	   1 2
      2    2 1 4 3   2      -- added row clues
   	       3 4 1 2 	 2
      1    4 2 3 1
   	       1 3 2 4
   	   	       3
         */

        val clues = intArrayOf(
            0, 0, 1, 2,
            2, 2, 0, 0,
            0, 3, 0, 0,
            0, 1, 0, 2
        )

        val expected = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun rowPairClueTest() {

        /*     v-- removed this column clues
   	   	   	     2
      2    1 4 2 3   2      -- added row clues
   	       2 3 4 1 	 2
      1    4 1 3 2
   	       3 2 1 4

         */

        val clues = intArrayOf(
            0, 0, 0, 2,
            2, 2, 0, 0,
            0, 0, 0, 0,
            0, 1, 0, 2
        )

        val expected = arrayOf(
            intArrayOf(1, 4, 2, 3),
            intArrayOf(2, 3, 4, 1),
            intArrayOf(4, 1, 3, 2),
            intArrayOf(3, 2, 1, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun singleRowEndClueTest() {
        /*        v---- removed single column start clue
                1
            1 3 4 2
            2 4 1 3   2
            4 2 3 1     -- removed start clue
            3 1 2 4
                3
        */

        val clues = intArrayOf(
            0, 0, 1, 0,
            0, 2, 0, 0,
            0, 3, 0, 0,
            0, 0, 0, 0
        )

        val expected = arrayOf(
            intArrayOf(1, 3, 4, 2),
            intArrayOf(2, 4, 1, 3),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(3, 1, 2, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun singleRowStartClueTest() {
        /*        v---- removed single column start clue
                1
            1 3 4 2
         2  2 4 1 3     -- move clue to start
            4 2 3 1     -- removed start clue
            3 1 2 4
                3
        */

        val clues = intArrayOf(
            0, 0, 1, 0,
            0, 0, 0, 0,
            0, 3, 0, 0,
            0, 0, 2, 0
        )

        val expected = arrayOf(
            intArrayOf(1, 3, 4, 2),
            intArrayOf(2, 4, 1, 3),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(3, 1, 2, 4)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun singleColumnStartClueTest() {
        /*
                1
            1 2 4 3
            2 3 1 4
            3 4 2 1
            4 1 3 2
        */

        val clues = intArrayOf(
            0, 0, 1, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        )

        val expected = arrayOf(
            intArrayOf(1, 2, 4, 3),
            intArrayOf(2, 3, 1, 4),
            intArrayOf(3, 4, 2, 1),
            intArrayOf(4, 1, 3, 2)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun findSolutionFromGrid() {

        val clues = intArrayOf(
            0, 0, 1, 2,
            0, 2, 0, 0,
            0, 3, 0, 0,
            0, 1, 0, 0
        )

        val expected = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        val hints = Skyscrapers.Hints(clues)
        val cluesEngine = Skyscrapers.CluesEngine(hints)

        val startGrid = Skyscrapers.Grid.fromArrays(
            arrayOf(
                intArrayOf(0, 0, 4, 3),     // from the first iteration of example clues
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(0, 0, 2, 4)
            )
        )

        val res = cluesEngine.findSolutionByZeros(startGrid, 0)
        assertTrue(res.first)
        assertArrayEquals(expected, res.second.toArrays())
    }

}
