package online.vapcom.codewars.algorithms

import online.vapcom.codewars.algorithms.Skyscrapers.getVisibleFromEnd
import online.vapcom.codewars.algorithms.Skyscrapers.getVisibleFromStart
import online.vapcom.codewars.algorithms.Skyscrapers.packPermutationToInt
import online.vapcom.codewars.algorithms.Skyscrapers.permutations
import kotlin.test.Test
import kotlin.test.assertEquals

class SkyScraperTests {

    @Test
    fun permutationsFour() {
        val four = listOf(1, 2, 3, 4)
        val perms = four.permutations()
        assertEquals(24, perms.size)
        assertEquals("1, 2, 3, 4\n" +
                "2, 1, 3, 4\n" +
                "3, 1, 2, 4\n" +
                "1, 3, 2, 4\n" +
                "2, 3, 1, 4\n" +
                "3, 2, 1, 4\n" +
                "4, 2, 3, 1\n" +
                "2, 4, 3, 1\n" +
                "3, 4, 2, 1\n" +
                "4, 3, 2, 1\n" +
                "2, 3, 4, 1\n" +
                "3, 2, 4, 1\n" +
                "4, 1, 3, 2\n" +
                "1, 4, 3, 2\n" +
                "3, 4, 1, 2\n" +
                "4, 3, 1, 2\n" +
                "1, 3, 4, 2\n" +
                "3, 1, 4, 2\n" +
                "4, 1, 2, 3\n" +
                "1, 4, 2, 3\n" +
                "2, 4, 1, 3\n" +
                "4, 2, 1, 3\n" +
                "1, 2, 4, 3\n" +
                "2, 1, 4, 3", perms.joinToString("\n") { it.joinToString() })

        // check arithmetic progression sum: (1+4)*4/2 = 10
        perms.forEachIndexed { index, p ->
            assertEquals(10, p.sum(), "Sum of permutation #$index should be 10")
        }

        // check all permutations really differ
        val set = perms.map { it.joinToString() }.toSet()
        assertEquals(24, set.size)
    }

    @Test
    fun permutationsSeven() {
        val seven = listOf(1, 2, 3, 4, 5, 6, 7)
        val perms = seven.permutations()
        assertEquals(5040, perms.size)

        // check arithmetic progression sum: (1+7)*7/2 = 28
        perms.forEachIndexed { index, p ->
            assertEquals(28, p.sum(), "Sum of permutation #$index should be 28")
        }

        // check all differ
        val set = perms.map { it.joinToString() }.toSet()
        assertEquals(5040, set.size)
    }

    @Test
    fun packToIntTest() {
        assertEquals(0, packPermutationToInt(emptyList()))
        assertEquals(0x1, packPermutationToInt(listOf(1)))
        assertEquals(0x12, packPermutationToInt(listOf(1, 2)))
        assertEquals(0x1234, packPermutationToInt(listOf(1, 2, 3, 4)))
        assertEquals(0x123456, packPermutationToInt(listOf(1, 2, 3, 4, 5, 6)))
        assertEquals(0x1234567, packPermutationToInt(listOf(1, 2, 3, 4, 5, 6, 7)))
    }

    @Test
    fun visibleFrom() {
        val permutations = listOf(
            packPermutationToInt(listOf(1, 2, 3, 4)),
            packPermutationToInt(listOf(4, 3, 2, 1)),
            packPermutationToInt(listOf(4, 2, 3, 1)),
            packPermutationToInt(listOf(4, 1, 3, 2)),
            packPermutationToInt(listOf(3, 2, 1, 4)),
            packPermutationToInt(listOf(2, 1, 4, 3)),
            packPermutationToInt(listOf(2, 3, 1, 4))
        )

        assertEquals("4, 1, 1, 1, 2, 2, 3", getVisibleFromStart(permutations).joinToString())
        assertEquals("1, 4, 3, 3, 1, 2, 1", getVisibleFromEnd(permutations).joinToString())
    }

}
