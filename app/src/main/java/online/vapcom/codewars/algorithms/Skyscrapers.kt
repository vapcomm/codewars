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

    }

    /**
     * Main function, start point of solution finding
     */
    fun solvePuzzle(clues: IntArray): Array<IntArray> {
        println("== solvePuzzle: clues: ${clues.joinToString()}")
        val hints = Hints(clues)

        val cluesEngine = CluesEngine(hints)
        cluesEngine.makeGridGenerators()

        val result = cluesEngine.execute { grid, permutations ->
            findSolution(hints.emptyCluesColumns, 0, hints.emptyCluesRows, 0, grid, permutations)
        }

        println("== solvePuzzle: findSolutionCount: $findSolutionCount")

        return result
    }

    /**
     * First stage of solution, generates grids based on given clues
     */
    class CluesEngine(private val hints: Hints) {

        abstract class GridGenerator {
            protected abstract val perms: List<Long>
            fun permutationsSize(): Int = perms.size

            protected var currentPermutation = 0

            fun reset() {
                currentPermutation = 0
            }

            abstract fun makeGrid(startGrid: Array<IntArray>): Array<IntArray>

            fun hasNextGrid(startGrid: Array<IntArray>): Boolean {
                //TODO: change linear search to something faster
                while (currentPermutation < perms.size) {
                    if (isCurrentPermMatchGrid(startGrid)) {
                        return true
                    }
                    currentPermutation++
                }
                return false
            }

            abstract fun isCurrentPermMatchGrid(startGrid: Array<IntArray>): Boolean
        }

        abstract inner class PairClueGridGenerator(protected val pairClue: PairClue) : GridGenerator() {
            override val perms = permutations.filterIndexed { index, p ->
                startVisible[index] == pairClue.startClue && endVisible[index] == pairClue.endClue
            }
        }

        inner class PairColumnGridGenerator(pairClue: PairClue) : PairClueGridGenerator(pairClue) {
            override fun isCurrentPermMatchGrid(startGrid: Array<IntArray>): Boolean =
                columnPermutationMatchesGrid(pairClue.index, perms[currentPermutation], startGrid)

            override fun makeGrid(startGrid: Array<IntArray>): Array<IntArray> =
                putColumnToGrid(pairClue.index, perms[currentPermutation++], startGrid)
        }

        inner class PairRowGridGenerator(pairClue: PairClue) : PairClueGridGenerator(pairClue) {
            override fun isCurrentPermMatchGrid(startGrid: Array<IntArray>): Boolean =
                rowPermutationMatchesGrid(pairClue.index, perms[currentPermutation], startGrid)

            override fun makeGrid(startGrid: Array<IntArray>): Array<IntArray> =
                putRowToGrid(pairClue.index, perms[currentPermutation++], startGrid)
        }

        abstract inner class SingleClueGridGenerator(private val singleClue: SingleClue) : GridGenerator() {
            private val visible = if (singleClue.isStart) startVisible else endVisible

            override val perms = permutations.filterIndexed { index, p ->
                visible[index] == singleClue.clue
            }
        }

        inner class SingleColumnGridGenerator(private val singleClue: SingleClue) : SingleClueGridGenerator(singleClue) {
            override fun isCurrentPermMatchGrid(startGrid: Array<IntArray>): Boolean =
                columnPermutationMatchesGrid(singleClue.index, perms[currentPermutation], startGrid)

            override fun makeGrid(startGrid: Array<IntArray>): Array<IntArray> =
                putColumnToGrid(singleClue.index, perms[currentPermutation++], startGrid)
        }

        inner class SingleRowGridGenerator(private val singleClue: SingleClue) : SingleClueGridGenerator(singleClue) {
            override fun isCurrentPermMatchGrid(startGrid: Array<IntArray>): Boolean =
                rowPermutationMatchesGrid(singleClue.index, perms[currentPermutation], startGrid)

            override fun makeGrid(startGrid: Array<IntArray>): Array<IntArray> =
                putRowToGrid(singleClue.index, perms[currentPermutation++], startGrid)
        }

        // generate all possible columns and rows permutations of given grid size
        val permutations = Array(hints.gridSize) { it + 1 }.toList().permutations().map { packPermutationToLong(it) }.sorted()

        // Count visible skyscrapers for all permutations:
        // Start | Permutation | End:  4 | 1, 2, 3, 4 | 1
        val startVisible = getVisibleFromStart(permutations)
        val endVisible = getVisibleFromEnd(permutations)

        private val gridGenerators = ArrayList<GridGenerator>()

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
        fun execute(findSolutionBlock: (grid: Array<IntArray>, permutations: List<Long>) -> Pair<Boolean, Array<IntArray>>): Array<IntArray> {
            println("## execute: generators: ")
            gridGenerators.forEachIndexed { index, g ->
                println("#### gen[$index]: perms: ${g.permutationsSize()}")
            }
            println("#### total clues permutations: ${gridGenerators.fold(1L) { mult, ps -> mult * ps.permutationsSize().toLong()} }")

            val emptyGrid = Array(hints.gridSize) { IntArray(hints.gridSize) }

            if (gridGenerators.isEmpty()) {
                // no clues at all, do our best
                val res = findSolutionBlock(emptyGrid, permutations)
                return if (res.first) res.second else emptyGrid
            }

            val res = composeGrid(0, emptyGrid) { grid ->
                findSolutionBlock(grid, permutations)
            }

            println("## execute: compose count: $composeCount")

            return if (res.first) res.second else emptyGrid
        }

        private var composeCount = 0L

        /**
         * Recursively compose grid from all clue grid generators and try it with solution finder block
         */
        private fun composeGrid(genIndex: Int, startGrid: Array<IntArray>,
                                finderBlock: (grid: Array<IntArray>) -> Pair<Boolean, Array<IntArray>>): Pair<Boolean, Array<IntArray>> {
//            println("###### composeGrid: gen[$genIndex]: startGrid:\n${startGrid.toLogString()}")
//            println("###### composeGrid: gen[$genIndex]")

            composeCount++

            if (genIndex >= gridGenerators.size) {
                return finderBlock(startGrid)
            }

            val generator = gridGenerators[genIndex]
            generator.reset()

            while (generator.hasNextGrid(startGrid)) {
                val nextGrid = generator.makeGrid(startGrid)

                val res = composeGrid(genIndex + 1, nextGrid, finderBlock)
                if (res.first) return res
            }

            return Pair(false, emptyArray())
        }

    } // CluesEngine


    var findSolutionCount = 0L

    /**
     * Second iteration does recursive search of columns and rows with no clues from a given startGrid.
     */
    private fun findSolution(emptyColumns: List<Int>, columnOffset: Int, emptyRows: List<Int>, rowOffset: Int,
                             startGrid: Array<IntArray>, permutations: List<Long>): Pair<Boolean, Array<IntArray>> {
//        println("\n++++ findSolution: emptyColumns: $emptyColumns, coff:$columnOffset, emptyRows: $emptyRows, roff: $rowOffset," +
//                " start grid:\n${startGrid.toLogString()}")

        findSolutionCount++

        if (columnOffset < emptyColumns.size) { // columns cycle
//            println("++++++ findSolution: try column ${emptyColumns[columnOffset]}")
            val columnMask = packColumnToLong(emptyColumns[columnOffset], startGrid)
            //TODO: change linear search to the faster method
            permutations.forEach { perm ->
                if (perm and columnMask == columnMask) { // possible column permutation
                    val nextGrid = putColumnToGrid(emptyColumns[columnOffset], perm, startGrid)
                    if (isPossibleValidRows(nextGrid)) {
                        val res = findSolution(emptyColumns, columnOffset + 1, emptyRows, rowOffset, nextGrid, permutations)
                        if (res.first)
                            return res
                    }
                }
            }
        } else {
            if (rowOffset < emptyRows.size) { // rows cycle
//                println("++++++ findSolution: try row ${emptyRows[rowOffset]}")

                // empty columns may already fill the entire grid, check it
                val check = checkSolution(startGrid)
                if (check.first)
                    return check

                val rowMask = packRowToLong(startGrid[emptyRows[rowOffset]])
                //TODO: change linear search to the faster method
                permutations.forEach { perm ->
                    if (perm and rowMask == rowMask) {
                        val nextGrid = putRowToGrid(emptyRows[rowOffset], perm, startGrid)
                        val res = findSolution(emptyColumns, columnOffset, emptyRows, rowOffset + 1, nextGrid, permutations)
                        if (res.first)
                            return res
                    }
                }
            } else {
                // it was last empty row, check the final variant
                return checkSolution(startGrid)
            }
        }

        return Pair(false, emptyArray())
    }

    /**
     * Check all grid's rows have valid possible permutation values,
     * i.e. all non-zero numbers in row should appear only once
     */
    internal fun isPossibleValidRows(grid: Array<IntArray>): Boolean {
        grid.forEach { row ->
            var bits = 0
            row.forEach { v ->
                if (v > 0) {    // skip zeros
                    val bit = 1 shl v
                    if (bits and bit != 0) { // already have this number
                        return false
                    } else bits = bits or bit
                }
            }
        }

        return true
    }

    /**
     * Strong check correctness of all columns in grid (without zeros)
     */
    internal fun isValidColumns(grid: Array<IntArray>): Boolean {
        for (i in grid[0].indices) {
            var bits = 0
            grid.forEach { row ->
                if (row[i] == 0)
                    return false

                val bit = 1 shl row[i]
                if (bits and bit != 0) { // already have this number
                    return false
                } else bits = bits or bit
            }
        }

        return true
    }

    /**
     * Return true and given grid if it is a final solution, all columns and rows have correct permutations
     */
    private fun checkSolution(grid: Array<IntArray>): Pair<Boolean, Array<IntArray>> {
        println("====== CHECK:\n${grid.toLogString()}")
        return if (isValidColumns(grid) && isPossibleValidRows(grid)) {
            println("====== FOUND SOLUTION!")
            Pair(true, grid)
        } else Pair(false, emptyArray())
    }

    /**
     * Pack one permutation array to Long, last value to the least value byte
     *
     * NOTE: permutation.size should be <= 8
     */
    fun packPermutationToLong(permutation: List<Int>): Long {
        var result = 0L
        permutation.forEach { result = (result shl 8) or (it.toLong() and 0xFFL) }
        return result
    }

    /**
     * Pack grid's column to Long, last value to the least value byte
     */
    private fun packColumnToLong(column: Int, grid: Array<IntArray>): Long {
        var result = 0L
        grid.forEach { result = (result shl 8) or (it[column].toLong() and 0xFFL) }
        return result
    }

    /**
     * Pack row to Long, last value to the least value byte
     */
    private fun packRowToLong(row: IntArray): Long {
        var result = 0L
        row.forEach { result = (result shl 8) or (it.toLong() and 0xFFL) }
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
    internal fun getVisibleFromStart(permutations: List<Long>): List<Int> = getVisibleFrom(permutations, 7 downTo 0)

    /**
     * Return list of visible skyscrapers for each permutation from it's end
     */
    internal fun getVisibleFromEnd(permutations: List<Long>): List<Int> = getVisibleFrom(permutations, 0..7)

    private fun getVisibleFrom(permutations: List<Long>, interval: IntProgression): List<Int> {
        return permutations.map { p ->
            var visible = 0
            var maxHeight = 0
            for (i in interval) {
                val height = ((p shr (i * 8)) and 0xFF).toInt()
                if (height > maxHeight) {
                    maxHeight = height
                    visible++
                }
            }

            visible
        }
    }

    private fun putColumnToGrid(column: Int, permutation: Long, grid: Array<IntArray>): Array<IntArray> {
        val newGrid = grid.copy()
        val maxIndex = newGrid.size - 1
        for (i in newGrid.indices) {
            newGrid[i][column] = (permutation shr (8 * (maxIndex - i))).toInt() and 0xFF
        }
        return newGrid
    }

    private fun putRowToGrid(row: Int, permutation: Long, grid: Array<IntArray>): Array<IntArray> {
        val newGrid = grid.copy()
        val maxIndex = newGrid.size - 1
        for (i in newGrid.indices) {
            newGrid[row][i] = (permutation shr (8 * (maxIndex - i))).toInt() and 0xFF
        }
        return newGrid
    }

    /**
     * Deep copy of grid
     */
    fun Array<IntArray>.copy(): Array<IntArray> = Array(this.size) { this[it].copyOf() }

    /**
     * Return true if given row permutation matches given grid and can be inserted into it
     */
    internal fun rowPermutationMatchesGrid(row: Int, permutation: Long, grid: Array<IntArray>): Boolean {
        val maxIndex = grid[row].size - 1
        grid[row].forEachIndexed { index, i ->
            if (i != 0 && i != ((permutation shr (8 * (maxIndex - index))) and 0xFF).toInt())
                return false
        }
        return true
    }

    /**
     * Return true if given column permutation matches given grid and can be inserted into it
     */
    internal fun columnPermutationMatchesGrid(column: Int, permutation: Long, grid: Array<IntArray>): Boolean {
        val maxIndex = grid.size - 1
        grid.forEachIndexed { index, row ->
            if (row[column] != 0 && row[column] != ((permutation shr (8 * (maxIndex - index))) and 0xFF).toInt())
                return false
        }
        return true
    }

}

fun Array<IntArray>.toLogString(): String = this.joinToString("\n") { it.joinToString(" ") }
