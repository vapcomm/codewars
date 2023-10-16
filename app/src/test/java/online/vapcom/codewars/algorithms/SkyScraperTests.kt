package online.vapcom.codewars.algorithms

import online.vapcom.codewars.algorithms.Skyscrapers.columnPermutationMatchesGrid
import online.vapcom.codewars.algorithms.Skyscrapers.copyTo
import online.vapcom.codewars.algorithms.Skyscrapers.getVisibleFromEnd
import online.vapcom.codewars.algorithms.Skyscrapers.getVisibleFromStart
import online.vapcom.codewars.algorithms.Skyscrapers.isValidColumns
import online.vapcom.codewars.algorithms.Skyscrapers.isPossibleValidRows
import online.vapcom.codewars.algorithms.Skyscrapers.packPermutationToLong
import online.vapcom.codewars.algorithms.Skyscrapers.permutations
import online.vapcom.codewars.algorithms.Skyscrapers.rowPermutationMatchesGrid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun packToLongTest() {
        assertEquals(0, packPermutationToLong(emptyList()))
        assertEquals(0x01L, packPermutationToLong(listOf(1)))
        assertEquals(0x0102L, packPermutationToLong(listOf(1, 2)))
        assertEquals(0x01020304L, packPermutationToLong(listOf(1, 2, 3, 4)))
        assertEquals(0x010203040506L, packPermutationToLong(listOf(1, 2, 3, 4, 5, 6)))
        assertEquals(0x01020304050607L, packPermutationToLong(listOf(1, 2, 3, 4, 5, 6, 7)))
    }

    @Test
    fun visibleFrom() {
        val permutations = listOf(
            packPermutationToLong(listOf(1, 2, 3, 4)),
            packPermutationToLong(listOf(4, 3, 2, 1)),
            packPermutationToLong(listOf(4, 2, 3, 1)),
            packPermutationToLong(listOf(4, 1, 3, 2)),
            packPermutationToLong(listOf(3, 2, 1, 4)),
            packPermutationToLong(listOf(2, 1, 4, 3)),
            packPermutationToLong(listOf(2, 3, 1, 4))
        )

        assertEquals("4, 1, 1, 1, 2, 2, 3", getVisibleFromStart(permutations).joinToString())
        assertEquals("1, 4, 3, 3, 1, 2, 1", getVisibleFromEnd(permutations).joinToString())
    }

    @Test
    fun gridCopy() {
        val twos = arrayOf(
            intArrayOf(2, 2, 2, 2),
            intArrayOf(2, 2, 2, 2),
            intArrayOf(2, 2, 2, 2),
            intArrayOf(2, 2, 2, 2)
        )

        val ones = Array(4) { IntArray(4) }
        twos.copyTo(ones)
        ones.forEach { it.fill(1) }

        assertEquals("1, 1, 1, 1\n" +
                "1, 1, 1, 1\n" +
                "1, 1, 1, 1\n" +
                "1, 1, 1, 1",
            ones.joinToString("\n") { it.joinToString() })

        // zeros shouldn't change on deep copy
        assertEquals("2, 2, 2, 2\n" +
                "2, 2, 2, 2\n" +
                "2, 2, 2, 2\n" +
                "2, 2, 2, 2",
            twos.joinToString("\n") { it.joinToString() })
    }

    @Test
    fun rowGridMatchers() {
        val zeros = arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0)
        )

        val perm0 = 0x01020304L
        assertTrue(rowPermutationMatchesGrid(0, perm0, zeros))

        val oneColumn = arrayOf(
            intArrayOf(0, 0, 3, 0),
            intArrayOf(0, 0, 2, 0),
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 4, 0)
        )
        assertTrue(rowPermutationMatchesGrid(0, perm0, oneColumn))
        assertFalse(rowPermutationMatchesGrid(1, perm0, oneColumn))

        val twoColumns = arrayOf(
            intArrayOf(1, 0, 3, 0),
            intArrayOf(4, 0, 2, 0),
            intArrayOf(3, 0, 1, 0),
            intArrayOf(2, 0, 4, 0)
        )
        assertTrue(rowPermutationMatchesGrid(0, perm0, twoColumns))
        assertFalse(rowPermutationMatchesGrid(1, perm0, twoColumns))

        val perm3 = 0x02030401L
        assertTrue(rowPermutationMatchesGrid(3, perm3, twoColumns))
    }

    @Test
    fun columnGridMatchers() {
        val zeros = arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0)
        )

        val perm0 = 0x01020304L
        assertTrue(columnPermutationMatchesGrid(0, perm0, zeros))

        val oneRow = arrayOf(
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(3, 2, 1, 4),
            intArrayOf(0, 0, 0, 0)
        )
        assertTrue(columnPermutationMatchesGrid(0, perm0, oneRow))
        assertFalse(columnPermutationMatchesGrid(1, perm0, oneRow))

        val twoRows = arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(0, 0, 2, 0),
            intArrayOf(3, 0, 1, 2),
            intArrayOf(0, 0, 4, 1)
        )
        assertTrue(columnPermutationMatchesGrid(0, perm0, twoRows))
        assertFalse(columnPermutationMatchesGrid(1, perm0, twoRows))

        val perm3 = 0x04030201L
        assertTrue(columnPermutationMatchesGrid(3, perm3, twoRows))
    }


    @Test
    fun rowsValidation() {

        val validFull = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        assertTrue(isPossibleValidRows(validFull))

        val validNotFull = arrayOf(
            intArrayOf(2, 1, 0, 3),
            intArrayOf(3, 4, 0, 2),
            intArrayOf(4, 0, 3, 1),
            intArrayOf(0, 3, 2, 4)
        )

        assertTrue(isPossibleValidRows(validNotFull))


        val invalid1 = arrayOf(
            intArrayOf(1, 2, 4, 1)
        )
        val invalid2 = arrayOf(
            intArrayOf(3, 3, 1, 4),
            intArrayOf(4, 1, 3, 2)
        )
        val invalid3 = arrayOf(
            intArrayOf(4, 1, 3, 2),
            intArrayOf(2, 4, 2, 3)
        )

        assertFalse(isPossibleValidRows(invalid1))
        assertFalse(isPossibleValidRows(invalid2))
        assertFalse(isPossibleValidRows(invalid3))

        val invalidNotFull = arrayOf(
            intArrayOf(4, 1, 3, 0),
            intArrayOf(2, 4, 2, 0)
        )

        assertFalse(isPossibleValidRows(invalidNotFull))
    }

    @Test
    fun columnsValidation() {
        val valid = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )

        assertTrue(isValidColumns(valid))

        val invalid = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 2, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 4)
        )
        assertFalse(isValidColumns(invalid))

        val oneZero = arrayOf(
            intArrayOf(2, 1, 4, 3),
            intArrayOf(3, 4, 1, 2),
            intArrayOf(4, 2, 3, 1),
            intArrayOf(1, 3, 2, 0)
        )
        assertFalse(isValidColumns(oneZero))
    }

}
