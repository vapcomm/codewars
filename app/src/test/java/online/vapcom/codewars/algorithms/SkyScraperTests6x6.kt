package online.vapcom.codewars.algorithms

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class SkyScraperTests6x6 {

    @Test
    fun exampleTest() {
        // example data from https://www.codewars.com/kata/6-by-6-skyscrapers/
        /*
           2 2
   	 5 6 1 4 3 2
   	 4 1 3 2 6 5
 3 	 2 3 6 1 5 4
   	 6 5 4 3 2 1 	 6
 4 	 1 2 5 6 4 3 	 3
 4 	 3 4 2 5 1 6
             4
         */

        val clues = intArrayOf(
            0, 0, 0, 2, 2, 0,
            0, 0, 0, 6, 3, 0,
            0, 4, 0, 0, 0, 0,
            4, 4, 0, 3, 0, 0
        )

        val expected = arrayOf(
            intArrayOf(5, 6, 1, 4, 3, 2),
            intArrayOf(4, 1, 3, 2, 6, 5),
            intArrayOf(2, 3, 6, 1, 5, 4),
            intArrayOf(6, 5, 4, 3, 2, 1),
            intArrayOf(1, 2, 5, 6, 4, 3),
            intArrayOf(3, 4, 2, 5, 1, 6)
        )

        val actual = Skyscrapers.solvePuzzle(clues)
        assertArrayEquals(expected, actual)
    }

}
