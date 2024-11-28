package online.vapcom.codewars.algorithms


/**
 * #25 7×7 Skyscrapers
 *
 * We use universal algorithm for all this katas:
 *```
 * https://www.codewars.com/kata/4-by-4-skyscrapers/		-- 4 kyu
 * https://www.codewars.com/kata/6-by-6-skyscrapers/		-- 2 kyu
 * https://www.codewars.com/kata/5917a2205ffc30ec3a0000a8	-- 1 kyu
 * ```
 */
object Skyscrapers {

    /**
     * Main function, start point of solution finding
     */
    fun solvePuzzle(clues: IntArray): Array<IntArray> {
        println("== solvePuzzle: clues: ${clues.joinToString()}")
        val hints = Hints(clues)

        val cluesEngine = CluesEngine(hints)
        cluesEngine.makeGridGenerators()

        val result = cluesEngine.execute { grid, permutations, gridCache ->
            findSolution(hints.emptyCluesColumns, 0, hints.emptyCluesRows, 0, grid, permutations, gridCache)
        }

        println("== solvePuzzle: permutations: ${cluesEngine.permutations.size}, findSolutionCount: $findSolutionCount,\n" +
                " putColumnToGridCount: $putColumnToGridCount, isPossibleValidRowsCounter: $isPossibleValidRowsCounter," +
                " putRowToGrid: $putRowToGrid")

        return result.toArrays()
    }


    data class PairClue(
        val index: Int,         // column or row index in grid
        val startClue: Int,     // start and end clue values
        val endClue: Int
    )

    data class SingleClue(val index: Int, val isStart: Boolean, val clue: Int)

    /**
     * Helper class to keep all parsed clues data
     */
    class Hints(clues: IntArray) {
        val gridSize: Int = when (clues.size) {
            16 -> 4
            24 -> 6
            28 -> 7
            //NOTE: current implementation supports up to 8x8 grid, see packPermutationToLong()
            else -> throw IllegalArgumentException("Unsupported clues size ${clues.size}")
        }

        private val topClues = clues.copyOfRange(0, gridSize)
        private val rightClues = clues.copyOfRange(gridSize, gridSize * 2)
        private val bottomClues = clues.copyOfRange(gridSize * 2, gridSize * 3).reversedArray()
        private val leftClues = clues.copyOfRange(gridSize * 3, clues.size).reversedArray()

        init {
            println("-- top: ${topClues.joinToString()}, right: ${rightClues.joinToString()}, " +
                    "bottom: ${bottomClues.joinToString()}, left: ${leftClues.joinToString()}")
        }


        private fun pairTransform(index: Int, a: Int, b: Int): PairClue =
            if(a > 0 && b > 0) PairClue(index, a, b)
            else PairClue(-1, 0, 0)

        val pairCluesColumns = topClues.zipIndexed(bottomClues, ::pairTransform).filter { it.index >= 0 }
        val pairCluesRows = leftClues.zipIndexed(rightClues, ::pairTransform).filter { it.index >= 0 }

        private fun singleTransform(index: Int, a: Int, b: Int): SingleClue = when {
            a > 0 && b == 0 -> SingleClue(index, true, a)
            a == 0 && b > 0 -> SingleClue(index, false, b)
            else -> SingleClue(-1, false, 0)
        }

        val singleCluesColumns = topClues.zipIndexed(bottomClues, ::singleTransform).filter { it.index >= 0 }
        val singleCluesRows = leftClues.zipIndexed(rightClues, ::singleTransform).filter { it.index >= 0 }

        private fun emptyTransform(index: Int, a: Int, b: Int): Int =
            if (a == 0 && b == 0) index else -1

        val emptyCluesColumns = topClues.zipIndexed(bottomClues, ::emptyTransform).filter { it >= 0 }
        val emptyCluesRows = leftClues.zipIndexed(rightClues, ::emptyTransform).filter { it >= 0 }

        init {
            println("-- pairs: columns: $pairCluesColumns, rows: $pairCluesRows")
            println("-- singles: columns: $singleCluesColumns, rows: $singleCluesRows")
            println("-- empty: columns: $emptyCluesColumns, rows: $emptyCluesRows")
        }

        private inline fun <V> IntArray.zipIndexed(other: IntArray, transform: (index: Int, a: Int, b: Int) -> V): List<V> {
            val size = minOf(size, other.size)
            val list = ArrayList<V>(size)
            for (i in 0 until size) {
                list.add(transform(i, this[i], other[i]))
            }
            return list
        }

    } // Hints

    /**
     * Compact and fast grid storage.
     * @param size grid size, it should be <= 8
     */
    class Grid(private val size: Int) {

        companion object {
            val EMPTY: Grid = Grid(0)

            fun fromArrays(src: Array<IntArray>): Grid {
                val g = Grid(src.size)
                src.forEachIndexed { ri, row ->
                    g.grid[ri] = packRowToInt(row)
                }
                return g
            }

            /**
             * Pack row from IntArray to Int, last value to the least value tetrad
             */
            private fun packRowToInt(row: IntArray): Int {
                var result = 0
                row.forEach { result = (result shl 4) or (it and 0x0F) }
                return result
            }
        }

        val grid = IntArray(size)   // rows packed in Ints

        /**
         * Return a deep copy of this grid
         */
        fun clone(): Grid {
            val g = Grid(this.size)
            grid.copyInto(g.grid)
            return g
        }

        /**
         * Return true if given column permutation matches grid and can be inserted into it
         */
        fun columnPermutationMatches(column: Int, permutation: Int): Boolean {
            val maxIndex = size - 1
            val shift = 4 * (size - 1 - column)
            for (row in 0 until size) {
                val cell = (grid[row] shr shift) and 0x0F
                if (cell != 0 && cell != (permutation shr (4 * (maxIndex - row))) and 0x0F)
                    return false
            }

            return true
        }

        /**
         * Copy this grid to dst and put permutation on column
         */
        fun putColumnToGrid(column: Int, permutation: Int, dst: Grid) {
//+++
            putColumnToGridCount++
//+++
            val shift = 4 * (size - 1 - column)
            val maxIndex = size - 1
            for (row in 0 until size) {
                val p = (((permutation shr (4 * (maxIndex - row))) and 0x0F) shl (4 * (maxIndex - column)))
                dst.grid[row] = (grid[row] and (0x0F shl shift).inv()) or p
            }
        }

        fun packColumnToInt(column: Int): Int {
            val shift = 4 * (size - 1 - column)
            var result = 0

            for (row in 0 until size) {
                result = (result shl 4) or ((grid[row] shr shift) and 0xF)
            }
            return result
        }

        fun getRow(row: Int): Int = grid[row]

        /**
         * Return true if given row permutation matches grid and can be inserted into it
         */
        fun rowPermutationMatches(rowIndex: Int, permutation: Int): Boolean {
            val row = grid[rowIndex]
            //TODO: make wise bits check without loop and shifts
            for (i in 0 until size) {
                val cell = (row shr (4 * i)) and 0x0F
                if (cell != 0 && cell != (permutation shr (4 * i)) and 0x0F)
                    return false
            }

            return true
        }

        fun putRowToGrid(row: Int, permutation: Int, dst: Grid) {
//+++
            putRowToGrid++
//+++
            grid.copyInto(dst.grid)
            dst.grid[row] = permutation
        }

        /**
         * Check all grid's rows have valid possible permutation values,
         * i.e. all non-zero numbers in row should appear only once
         */
        fun isPossibleValidRows(): Boolean {
//+++
            isPossibleValidRowsCounter++
//+++
            grid.forEach { row ->
                var bits = 0
                for (i in 0 until size) {
                    val cell = (row shr (4 * i)) and 0x0F
                    if (cell > 0) { // skip zeros
                        val bit = 1 shl cell
                        if (bits and bit != 0) { // already have this number
                            return false
                        } else bits = bits or bit
                    }
                }
            }

            return true
        }

        /**
         * Strong correctness check of all columns in grid (without zeros)
         */
        fun isValidColumns(): Boolean {
            for (column in 0 until size) {
                var bits = 0
                grid.forEach { row ->
                    val cell = (row shr (4 * (size - 1 - column))) and 0x0F
                    if (cell == 0)
                        return false

                    val bit = 1 shl cell
                    if (bits and bit != 0) { // already have this number
                        return false
                    } else bits = bits or bit
                }
            }
            
            return true
        }

        /**
         * Return true if it is a final solution, all columns and rows have correct permutations
         */
        fun checkSolution(): Boolean {
//            println("====== CHECK:\n${toLogString()}")
            return if (isValidColumns() && isPossibleValidRows()) {
                println("====== FOUND SOLUTION!")
                true
            } else false
        }

        /**
         * Convert internal grid to required result format
         */
        fun toArrays(): Array<IntArray> {
            return Array(size) { r ->
                IntArray(size) { c ->
                    (grid[r] shr (4 * (size - 1 - c))) and 0x0F
                }
            }
        }

        /**
         * Return value with bits set to 1 if column has this number.
         *
         * Example: 1234 -> 11110b, 1030 -> 1010b
         */
        fun getColumnValuesMask(columnIndex: Int): Int {
            val shift = 4 * (size - 1 - columnIndex)
            var bits = 0
            for (row in 0 until size) {
                val cell = ((grid[row] shr shift) and 0xF)
                if (cell > 0)
                    bits = bits or (1 shl cell)
            }
            return bits
        }

        /**
         * Return value with bits set to 1 if row has this number.
         *
         * Example: 1234 -> 11110b, 1030 -> 1010b
         */
        fun getRowValuesMask(rowIndex: Int): Int {
            val row = grid[rowIndex]
            var bits = 0
            for (i in 0 until grid.size) {
                val cell = (row shr (4 * i)) and 0x0F
                if (cell > 0)
                    bits = bits or (1 shl cell)
            }
            return bits
        }

        /**
         * Return true if column contains at least one zero
         */
        fun columnNotFull(columnIndex: Int): Boolean {
            val shift = 4 * (size - 1 - columnIndex)
            for (row in 0 until size) {
                val cell = ((grid[row] shr shift) and 0xF)
                if (cell == 0)
                    return true
            }
            return false
        }

//+++
        @OptIn(ExperimentalStdlibApi::class)
        fun toLogString(): String = grid.joinToString("\n") { it.toHexString() }
//+++
    }


    /**
     * First stage of solution, generates grids based on given clues
     */
    class CluesEngine(private val hints: Hints) {

        /**
         * Base class for a first stage grid generators
         */
        abstract class GridGenerator(gridSize: Int) {
            protected abstract val perms: List<Int>
            fun permutationsSize(): Int = perms.size

            protected var currentPermutation = 0
            // buffer for new generated grids to not to allocate them during grid compose
            val nextGrid = Grid(gridSize)

            fun reset() {
                currentPermutation = 0
            }

            fun hasNextGrid(startGrid: Grid): Boolean {
                //TODO: change linear search to something faster
                while (currentPermutation < perms.size) {
                    if (isCurrentPermMatchGrid(startGrid)) {
                        return true
                    }
                    currentPermutation++
                }
                return false
            }

            abstract fun makeGrid(startGrid: Grid, nextGrid: Grid)
            abstract fun isCurrentPermMatchGrid(startGrid: Grid): Boolean
        }

        /**
         * Base class for pair clues grid generators
         */
        abstract inner class PairClueGridGenerator(protected val pairClue: PairClue) : GridGenerator(hints.gridSize) {
            override val perms = permutations.filterIndexed { index, _ ->
                startVisible[index] == pairClue.startClue && endVisible[index] == pairClue.endClue
            }
        }

        inner class PairColumnGridGenerator(pairClue: PairClue) : PairClueGridGenerator(pairClue) {
            override fun isCurrentPermMatchGrid(startGrid: Grid): Boolean =
                startGrid.columnPermutationMatches(pairClue.index, perms[currentPermutation])

            override fun makeGrid(startGrid: Grid, nextGrid: Grid) =
                startGrid.putColumnToGrid(pairClue.index, perms[currentPermutation++], nextGrid)
        }

        inner class PairRowGridGenerator(pairClue: PairClue) : PairClueGridGenerator(pairClue) {
            override fun isCurrentPermMatchGrid(startGrid: Grid): Boolean =
                startGrid.rowPermutationMatches(pairClue.index, perms[currentPermutation])

            override fun makeGrid(startGrid: Grid, nextGrid: Grid) =
                startGrid.putRowToGrid(pairClue.index, perms[currentPermutation++], nextGrid)
        }

        /**
         * Base class for single clue grid generators
         */
        abstract inner class SingleClueGridGenerator(private val singleClue: SingleClue) : GridGenerator(hints.gridSize) {
            private val visible = if (singleClue.isStart) startVisible else endVisible

            override val perms = permutations.filterIndexed { index, _ ->
                visible[index] == singleClue.clue
            }
        }

        inner class SingleColumnGridGenerator(private val singleClue: SingleClue) : SingleClueGridGenerator(singleClue) {
            override fun isCurrentPermMatchGrid(startGrid: Grid): Boolean =
                startGrid.columnPermutationMatches(singleClue.index, perms[currentPermutation])

            override fun makeGrid(startGrid: Grid, nextGrid: Grid) =
                startGrid.putColumnToGrid(singleClue.index, perms[currentPermutation++], nextGrid)
        }

        inner class SingleRowGridGenerator(private val singleClue: SingleClue) : SingleClueGridGenerator(singleClue) {
            override fun isCurrentPermMatchGrid(startGrid: Grid): Boolean =
                startGrid.rowPermutationMatches(singleClue.index, perms[currentPermutation])

            override fun makeGrid(startGrid: Grid, nextGrid: Grid) =
                startGrid.putRowToGrid(singleClue.index, perms[currentPermutation++], nextGrid)
        }

        // generate all possible columns and rows permutations of given grid size
        val permutations = Array(hints.gridSize) { it + 1 }.toList().permutations().map { packPermutationToInt(it) }.sorted()

        // Count visible skyscrapers for all permutations:
        // Start | Permutation | End:  4 | 1, 2, 3, 4 | 1
        val startVisible = getVisibleFromStart(permutations)
        val endVisible = getVisibleFromEnd(permutations)

        private val gridGenerators = ArrayList<GridGenerator>()

        // pre allocated grids for findSolution()
        private val gridsCache = Array(hints.gridSize) { Array(hints.gridSize) { Grid(hints.gridSize) } }

        /**
         * Make generators list based on clues. Pair clues go first.
         */
        fun makeGridGenerators() {
            hints.pairCluesColumns.forEach { pairClue ->
                gridGenerators.add(PairColumnGridGenerator(pairClue))
            }

            hints.pairCluesRows.forEach { pairClue ->
                gridGenerators.add(PairRowGridGenerator(pairClue))
            }

            hints.singleCluesColumns.forEach { singleClue ->
                gridGenerators.add(SingleColumnGridGenerator(singleClue))
            }

            hints.singleCluesRows.forEach { singleClue ->
                gridGenerators.add(SingleRowGridGenerator(singleClue))
            }
        }

        /**
         * Compose first stage girds based on given clues and call findSolutionBlock() to find final solution
         */
        fun execute(findSolutionBlock: (grid: Grid, permutations: List<Int>, gridsCache: Array<Array<Grid>>) -> Pair<Boolean, Grid>): Grid {
            println("## execute: generators: ")
            gridGenerators.forEachIndexed { index, g ->
                println("#### gen[$index]: perms: ${g.permutationsSize()}")
            }
            println("#### total clues permutations: ${gridGenerators.fold(1L) { mult, ps -> mult * ps.permutationsSize().toLong()} }")

            val emptyGrid = Grid(hints.gridSize)

            if (gridGenerators.isEmpty()) {
                // no clues at all, do our best
                val res = findSolutionBlock(emptyGrid, permutations, gridsCache)
                return if (res.first) res.second else emptyGrid
            }

            val res = composeGrid(0, emptyGrid) { grid ->
                //findSolutionBlock(grid, permutations, gridsCache)
                findSolutionByZeros(grid, 0)
            }

            println("## execute: compose count: $composeCount")

            return if (res.first) res.second else emptyGrid
        }

        private val oneInverted = 1.inv()
        private val sizeMask = (-1 shl (hints.gridSize + 1)).inv()

        @OptIn(ExperimentalStdlibApi::class)
        fun findSolutionByZeros(startGrid: Grid, c: Int): Pair<Boolean, Grid> {
            findSolutionCount++

            if (c >= startGrid.grid.size) // all columns passed, check solution
                return if (startGrid.checkSolution()) Pair(true, startGrid) else gridNotFound

            val columnBits = startGrid.getColumnValuesMask(c)
            val column = startGrid.packColumnToInt(c)
//            println("++++ findSolutionByZeros: column[$c]: ${column.toHexString()}, startGrid:\n${startGrid.toLogString()}")

            val maxIndex = startGrid.grid.size - 1
            for (r in 0 until startGrid.grid.size) {
                val cell = (column shr (4 * (maxIndex - r))) and 0x0F
                if (cell == 0) {
                    // get possible cell values from row r
                    val rowBits = startGrid.getRowValuesMask(r)
                    val bothUnknownBits = columnBits.inv() and rowBits.inv() and oneInverted and sizeMask
//                    println("++++ findSolutionByZeros: [$r][$c] columnBits: ${columnBits.toHexString()}, rowBits: ${rowBits.toHexString()}," +
//                            " both: ${bothUnknownBits.toHexString()}, sizeMask: ${sizeMask.toHexString()}")

                    if (bothUnknownBits == 0) {
                        // can't find solution for this cell, skip variant
//                        println("++++ findSolutionByZeros: bothUnknownBits ZERO, skip variant")
                        return gridNotFound
                    }

                    val cellVariants = cellVariantsFromBits(bothUnknownBits)
//                    println("++++ findSolutionByZeros: [$r][$c] cellVariants: $cellVariants")

                    val lShift = 4 * (maxIndex - r)
                    val maskedColumn = column and ((0x0F shl lShift).inv())
                    cellVariants.forEach { cv ->
                        val newColumn = maskedColumn or (cv shl lShift)

//                        println("++++ findSolutionByZeros: [$r][$c] try variant: $cv, newColumn: ${newColumn.toHexString()}, gridsCache[$r][$c]")

                        val nextGrid = gridsCache[r][c]
                        startGrid.putColumnToGrid(c, newColumn, nextGrid)
                        val res = findSolutionByZeros(nextGrid, c)
                        if (res.first) return res
                    }
                }
            }

            if (startGrid.columnNotFull(c)) {
                // column[$c] not full after row loop, skip
//                println("++++ findSolutionByZeros: column[$c] not full after row loop, skip")
                return gridNotFound
            }

//            println("++++ findSolutionByZeros: column[$c] filled, switch to next")

            // this column doesn't contain any zeros, proceed with next
            return findSolutionByZeros(startGrid, c + 1)
        }

        private fun cellVariantsFromBits(unknownBits: Int): ArrayList<Int> {
            val result = ArrayList<Int>()
            var bits = unknownBits shr 1    // skip zero's bit
            for (i in 1..hints.gridSize) {
                if (bits and 1 != 0)
                    result.add(i)
                bits = bits shr 1
            }
            return result
        }

        private var composeCount = 0L

        /**
         * Recursively compose grid from all clue grid generators and try it with solution finder block
         */
        private fun composeGrid(genIndex: Int, startGrid: Grid, finderBlock: (grid: Grid) -> Pair<Boolean, Grid>): Pair<Boolean, Grid> {
//            println("###### composeGrid: gen[$genIndex]: startGrid:\n${startGrid.toLogString()}")
//            println("###### composeGrid: gen[$genIndex]")

            composeCount++

            if (genIndex >= gridGenerators.size) {
                return finderBlock(startGrid)
            }

            val generator = gridGenerators[genIndex]
            generator.reset()

            while (generator.hasNextGrid(startGrid)) {
                generator.makeGrid(startGrid, generator.nextGrid)

                val res = composeGrid(genIndex + 1, generator.nextGrid, finderBlock)
                if (res.first) return res
            }
            return gridNotFound
        }

    } // CluesEngine


    var findSolutionCount = 0L

    private val gridNotFound = Pair(false, Grid.EMPTY)

    /**
     * Second iteration does recursive search of columns and rows with no clues from a given startGrid.
     */
    private fun findSolution(
        emptyColumns: List<Int>, columnOffset: Int, emptyRows: List<Int>, rowOffset: Int,
        startGrid: Grid, permutations: List<Int>, gridCache: Array<Array<Grid>>
    ): Pair<Boolean, Grid> {
        println("\n++++ findSolution: emptyColumns: $emptyColumns, coff:$columnOffset, emptyRows: $emptyRows, roff: $rowOffset," +
                " start grid:\n${startGrid.toLogString()}")

        findSolutionCount++

        if (columnOffset < emptyColumns.size) { // columns cycle
            println("++++++ findSolution: try column ${emptyColumns[columnOffset]}")
            val columnMask = startGrid.packColumnToInt(emptyColumns[columnOffset])
            //TODO: change linear search to the faster method
            permutations.forEach { perm ->
                if (perm and columnMask == columnMask) { // possible column permutation
                    val nextGrid = gridCache[rowOffset][columnOffset]
                    startGrid.putColumnToGrid(emptyColumns[columnOffset], perm, nextGrid)
                    if (nextGrid.isPossibleValidRows()) {
                        val res = findSolution(emptyColumns, columnOffset + 1, emptyRows, rowOffset, nextGrid, permutations, gridCache)
                        if (res.first)
                            return res
                    }
                }
            }
        } else {
            if (rowOffset < emptyRows.size) { // rows cycle
                println("++++++ findSolution: try row ${emptyRows[rowOffset]}")

                // empty columns may already fill the entire grid, check it
                if (startGrid.checkSolution())
                    return Pair(true, startGrid)

                val rowMask = startGrid.getRow(emptyRows[rowOffset])
                //TODO: change linear search to the faster method
                permutations.forEach { perm ->
                    if (perm and rowMask == rowMask) {
                        val nextGrid = gridCache[rowOffset][columnOffset]
                        startGrid.putRowToGrid(emptyRows[rowOffset], perm, nextGrid)
                        val res = findSolution(emptyColumns, columnOffset, emptyRows, rowOffset + 1, nextGrid, permutations, gridCache)
                        if (res.first)
                            return res
                    }
                }
            } else {
                // it was last empty row, check the final variant
                return if (startGrid.checkSolution()) Pair(true, startGrid) else gridNotFound
            }
        }

        return gridNotFound
    }

    /**
     * Pack one permutation array to Int, last value to the least value byte
     *
     * NOTE: permutation.size should be <= 8
     */
    fun packPermutationToInt(permutation: List<Int>): Int {
        var result = 0
        permutation.forEach { result = (result shl 4) or (it and 0x0F) }
        return result
    }

    /**
     *  Permutations generator by Heap’s algorithm
     */
    fun <V> List<V>.permutations(): List<List<V>> {
        val result = mutableListOf<List<V>>()

        fun swap(list: MutableList<V>, i: Int, j: Int) {
            list[i] = list.set(j, list[i])
        }

        fun generate(size: Int, src: MutableList<V>) {
            if (size <= 1) {
                result.add(src.toList())
            } else {
                for (i in 0 until size) {
                    generate(size - 1, src)
                    if (size % 2 == 0)
                        swap(src, i, size - 1)
                    else swap(src, 0, size - 1)
                }
            }
        }

        generate(this.size, this.toMutableList())
        return result
    }

    /**
     * Return list of visible skyscrapers for each permutation from it's start
     */
    internal fun getVisibleFromStart(permutations: List<Int>): List<Int> = getVisibleFrom(permutations, 7 downTo 0)

    /**
     * Return list of visible skyscrapers for each permutation from it's end
     */
    internal fun getVisibleFromEnd(permutations: List<Int>): List<Int> = getVisibleFrom(permutations, 0..7)

    private fun getVisibleFrom(permutations: List<Int>, interval: IntProgression): List<Int> {
        return permutations.map { p ->
            var visible = 0
            var maxHeight = 0
            for (i in interval) {
                val height = ((p shr (4 * i)) and 0xF)
                if (height > maxHeight) {
                    maxHeight = height
                    visible++
                }
            }

            visible
        }
    }

//+++
    var putColumnToGridCount = 0L
    var putRowToGrid = 0L
    var isPossibleValidRowsCounter = 0L
//+++

}

fun Array<IntArray>.toLogString(): String = this.joinToString("\n") { it.joinToString(" ") }
