package online.vapcom.codewars.strings

/**
 * #24 Most frequently used words in a text - 4 kyu
 *
 * https://www.codewars.com/kata/51e056fe544cf36c410000fb/train/kotlin
 */
fun top3(str: String): List<String> {

    fun plusOne(map: HashMap<String, Int>, word: String) {
        map.merge(word, 1) { old, value ->
            old + value
        }
    }

    println("\n== str: '$str'")

    val counters = hashMapOf<String, Int>()
    val sb = StringBuilder()
    var state = Top3State.SPACE

    str.forEach { c ->
        when (state) {
            Top3State.SPACE -> {
                if (c.isLetter()) {
                    sb.append(c)
                    state = Top3State.WORD
                } else if (c == '\'') {
                    sb.append(c)
                    state = Top3State.APOSTROPHE
                }
            }
            Top3State.WORD -> {
                if (c.isLetter() || c == '\'')
                    sb.append(c)
                else {
                    plusOne(counters, sb.toString().lowercase())
                    sb.clear()
                    state = Top3State.SPACE
                }
            }
            Top3State.APOSTROPHE -> {
                if (c.isLetter()) {
                    sb.append(c)
                    state = Top3State.WORD
                } else {
                    sb.clear()
                    state = Top3State.SPACE
                }
            }
        }
    }

    if (sb.isNotEmpty())    // last word
        plusOne(counters, sb.toString().lowercase())

    println("\n-- counters: $counters")

    if (counters.isEmpty())
        return emptyList()

    // find top3 words
    val maxes = Array(3) { "" }

    var m0 = 0
    var m1 = 0
    var m2 = 0

    counters.forEach { entry ->
        if (entry.value > m0) {
            m2 = m1; m1 = m0
            m0 = entry.value
            maxes[2] = maxes[1]
            maxes[1] = maxes[0]
            maxes[0] = entry.key
        } else if (entry.value > m1) {
            m2 = m1
            m1 = entry.value
            maxes[2] = maxes[1]
            maxes[1] = entry.key
        } else if (entry.value > m2) {
            m2 = entry.value
            maxes[2] = entry.key
        }
    }

    val result = maxes.filter { it.isNotEmpty() }
    println("-- MAX: maxes: ${maxes.joinToString()}, result: $result")

    return result
}

private enum class Top3State { SPACE, WORD, APOSTROPHE }
