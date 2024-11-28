package online.vapcom.codewars.numbers

object Fracts {

    // (1,2)(1,3)(1,4) -> (6,12)(4,12)(3,12)
    fun convertFrac(numbers: Array<LongArray>): String {

        val result = numbers
        return arraysToString(result)
    }

    private fun arraysToString(arrays: Array<LongArray>): String {
        val sb = StringBuilder()
        arrays.forEach { longs ->
            sb.append('(')
            sb.append(longs.joinToString(","))
            sb.append(')')
        }
        return sb.toString()
    }
}