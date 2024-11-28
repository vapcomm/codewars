package online.vapcom.codewars.algorithms

import online.vapcom.codewars.algorithms.Skyscrapers.Grid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalStdlibApi::class)
class SkyScraperGridTests {

    @Test
    fun gridFromArrays() {
        val ar = arrayOf(
            intArrayOf(1, 2, 3, 4),
            intArrayOf(2, 3, 4, 1),
            intArrayOf(4, 3, 2, 1),
            intArrayOf(3, 4, 1, 2)
        )

        val grid = Grid.fromArrays(ar)

        assertEquals("00001234\n" +
                "00002341\n" +
                "00004321\n" +
                "00003412",
            grid.grid.joinToString("\n") { it.toHexString() })
    }

    @Test
    fun rowGridMatchers() {
        val zeros = Grid.fromArrays(
            arrayOf(
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )

        val perm0 = 0x1234
        assertTrue(zeros.rowPermutationMatches(0, perm0))

        val oneColumn = Grid.fromArrays(
            arrayOf(
            intArrayOf(0, 0, 3, 0),
            intArrayOf(0, 0, 2, 0),
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 4, 0)
        ))
        assertTrue(oneColumn.rowPermutationMatches(0, perm0))
        assertFalse(oneColumn.rowPermutationMatches(1, perm0))
        assertFalse(oneColumn.rowPermutationMatches(2, perm0))
        assertFalse(oneColumn.rowPermutationMatches(3, perm0))

        val twoColumns = Grid.fromArrays(
            arrayOf(
            intArrayOf(1, 0, 3, 0),
            intArrayOf(4, 0, 2, 0),
            intArrayOf(3, 0, 1, 0),
            intArrayOf(2, 0, 4, 0)
        ))
        assertTrue(twoColumns.rowPermutationMatches(0, perm0))
        assertFalse(twoColumns.rowPermutationMatches(1, perm0))

        val perm3 = 0x2341
        assertTrue(twoColumns.rowPermutationMatches(3, perm3))
    }

    @Test
    fun columnGridMatchers() {
        val zeros = Grid.fromArrays(
            arrayOf(
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )

        val perm0 = 0x1234
        assertTrue(zeros.columnPermutationMatches(0, perm0))

        val oneRow = Grid.fromArrays(
            arrayOf(
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(3, 2, 1, 4),
                intArrayOf(0, 0, 0, 0)
            )
        )
        assertTrue(oneRow.columnPermutationMatches(0, perm0))
        assertFalse(oneRow.columnPermutationMatches(1, perm0))

        val twoRows = Grid.fromArrays(
            arrayOf(
                intArrayOf(1, 2, 3, 4),
                intArrayOf(0, 0, 2, 0),
                intArrayOf(3, 0, 1, 2),
                intArrayOf(0, 0, 4, 1)
            )
        )
        assertTrue(twoRows.columnPermutationMatches(0, perm0))
        assertFalse(twoRows.columnPermutationMatches(1, perm0))

        val perm3 = 0x4321
        assertTrue(twoRows.columnPermutationMatches(3, perm3))

        val perm4 = 0x3214
        assertTrue(twoRows.columnPermutationMatches(2, perm4))

    }

    @Test
    fun rowsValidation() {

        val validFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 4)
            )
        )
        assertTrue(validFull.isPossibleValidRows())

        val validNotFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 0, 3),
                intArrayOf(3, 4, 0, 2),
                intArrayOf(4, 0, 3, 1),
                intArrayOf(0, 3, 2, 4)
            )
        )
        assertTrue(validNotFull.isPossibleValidRows())

        val invalid1 = Grid.fromArrays(
            arrayOf(
                intArrayOf(1, 2, 4, 1),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )
        val invalid2 = Grid.fromArrays(
            arrayOf(
                intArrayOf(3, 3, 1, 4),
                intArrayOf(4, 1, 3, 2),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )
        val invalid3 = Grid.fromArrays(
            arrayOf(
                intArrayOf(4, 1, 3, 2),
                intArrayOf(2, 4, 2, 3),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )

        assertFalse(invalid1.isPossibleValidRows())
        assertFalse(invalid2.isPossibleValidRows())
        assertFalse(invalid3.isPossibleValidRows())

        val invalidNotFull = Grid.fromArrays(
            arrayOf(
            intArrayOf(4, 1, 3, 0),
            intArrayOf(2, 4, 2, 0),
            intArrayOf(0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0)
        ))

        assertFalse(invalidNotFull.isPossibleValidRows())
    }

    @Test
    fun columnsValidation() {
        val valid = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 4)
            )
        )

        assertTrue(valid.isValidColumns())

        val invalid = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 2, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 4)
            )
        )
        assertFalse(invalid.isValidColumns())

        val oneZero = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 0)
            )
        )
        assertFalse(oneZero.isValidColumns())
    }

    @Test
    fun putColumn() {
        val zeros = Grid.fromArrays(
            arrayOf(
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0)
            )
        )

        val one = zeros.clone()
        val two = zeros.clone()
        val three = zeros.clone()
        val four = zeros.clone()
        val five = zeros.clone()

        zeros.putColumnToGrid(0, 0x1234, one)

        assertEquals("00001000\n" +
                "00002000\n" +
                "00003000\n" +
                "00004000",
            one.grid.joinToString("\n") { it.toHexString() })

        one.putColumnToGrid(1, 0x2334, two)
        assertEquals("00001200\n" +
                "00002300\n" +
                "00003300\n" +
                "00004400",
            two.grid.joinToString("\n") { it.toHexString() })

        two.putColumnToGrid(2, 0x3421, three)
        assertEquals("00001230\n" +
                "00002340\n" +
                "00003320\n" +
                "00004410",
            three.grid.joinToString("\n") { it.toHexString() })

        three.putColumnToGrid(3, 0x4112, four)
        assertEquals("00001234\n" +
                "00002341\n" +
                "00003321\n" +
                "00004412",
            four.grid.joinToString("\n") { it.toHexString() })

        // replace column
        four.putColumnToGrid(1, 0x4321, five)
        assertEquals("00001434\n" +
                "00002341\n" +
                "00003221\n" +
                "00004112",
            five.grid.joinToString("\n") { it.toHexString() })

        // zeros should not change
        assertEquals("00000000\n" +
                "00000000\n" +
                "00000000\n" +
                "00000000",
            zeros.grid.joinToString("\n") { it.toHexString() })
    }

    @Test
    fun packColumns() {
        val grid = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 0, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 0, 2, 4)
            )
        )

        assertEquals(0x2341, grid.packColumnToInt(0))
        assertEquals(0x1420, grid.packColumnToInt(1))
        assertEquals(0x0132, grid.packColumnToInt(2))
        assertEquals(0x3214, grid.packColumnToInt(3))
    }

    @Test
    fun toArraysTest() {
        val grid = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 0, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 0, 2, 4)
            )
        )

        assertEquals("2 1 0 3\n" +
                "3 4 1 2\n" +
                "4 2 3 1\n" +
                "1 0 2 4",
            grid.toArrays().toLogString())
    }

    @Test
    fun rowBits() {
        val validFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 4)
            )
        )

        assertEquals(0b11110, validFull.getRowValuesMask(0))
        assertEquals(0b11110, validFull.getRowValuesMask(1))
        assertEquals(0b11110, validFull.getRowValuesMask(2))
        assertEquals(0b11110, validFull.getRowValuesMask(3))

        val validNotFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 0, 3),
                intArrayOf(3, 4, 0, 2),
                intArrayOf(4, 0, 3, 1),
                intArrayOf(0, 3, 2, 4)
            )
        )
        assertEquals(0b01110, validNotFull.getRowValuesMask(0))
        assertEquals(0b11100, validNotFull.getRowValuesMask(1))
        assertEquals(0b11010, validNotFull.getRowValuesMask(2))
        assertEquals(0b11100, validNotFull.getRowValuesMask(3))

        val invalid = Grid.fromArrays(
            arrayOf(
                intArrayOf(1, 2, 4, 1),
                intArrayOf(2, 2, 2, 2),
                intArrayOf(3, 4, 4, 3),
                intArrayOf(0, 0, 0, 0)
            )
        )

        assertEquals(0b10110, invalid.getRowValuesMask(0))
        assertEquals(0b00100, invalid.getRowValuesMask(1))
        assertEquals(0b11000, invalid.getRowValuesMask(2))
        assertEquals(0b00000, invalid.getRowValuesMask(3))
    }

    @Test
    fun columnBits() {
        val validFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 3),
                intArrayOf(3, 4, 1, 2),
                intArrayOf(4, 2, 3, 1),
                intArrayOf(1, 3, 2, 4)
            )
        )

        assertEquals(0b11110, validFull.getColumnValuesMask(0))
        assertEquals(0b11110, validFull.getColumnValuesMask(1))
        assertEquals(0b11110, validFull.getColumnValuesMask(2))
        assertEquals(0b11110, validFull.getColumnValuesMask(3))

        val validNotFull = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 1, 4, 0),
                intArrayOf(3, 4, 0, 2),
                intArrayOf(4, 0, 3, 1),
                intArrayOf(0, 3, 2, 0)
            )
        )
        assertEquals(0b11100, validNotFull.getColumnValuesMask(0))
        assertEquals(0b11010, validNotFull.getColumnValuesMask(1))
        assertEquals(0b11100, validNotFull.getColumnValuesMask(2))
        assertEquals(0b00110, validNotFull.getColumnValuesMask(3))

        val invalid = Grid.fromArrays(
            arrayOf(
                intArrayOf(1, 2, 2, 0),
                intArrayOf(2, 2, 2, 0),
                intArrayOf(4, 4, 2, 0),
                intArrayOf(1, 3, 2, 0)
            )
        )

        assertEquals(0b10110, invalid.getColumnValuesMask(0))
        assertEquals(0b11100, invalid.getColumnValuesMask(1))
        assertEquals(0b00100, invalid.getColumnValuesMask(2))
        assertEquals(0b00000, invalid.getColumnValuesMask(3))
    }

    @Test
    fun columnNotFull() {
        val grid = Grid.fromArrays(
            arrayOf(
                intArrayOf(2, 0, 4, 0),
                intArrayOf(3, 4, 0, 0),
                intArrayOf(4, 2, 3, 0),
                intArrayOf(1, 3, 2, 0)
            )
        )

        assertFalse(grid.columnNotFull(0))

        assertTrue(grid.columnNotFull(1))
        assertTrue(grid.columnNotFull(2))
        assertTrue(grid.columnNotFull(3))
    }

    }
