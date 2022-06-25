package online.vapcom.codewars.strings

/**
 * Two to One
 * https://www.codewars.com/kata/5656b6906de340bd1b0000ac/train/kotlin
 */
fun longest(s1: String, s2: String): String {

    val set = (s1 + s2).toSortedSet()
    val sb = StringBuilder(set.size)

    set.forEach {
        sb.append(it)
    }

    return sb.toString()
}

/**
 * Get the Middle Character
 * https://www.codewars.com/kata/56747fd5cb988479af000028/train/kotlin
 */
fun getMiddle(word: String): String {
    return if (word.isEmpty()) ""
    else {
        val half = word.length / 2
        if (word.length % 2 == 0) {
            word.substring(half - 1, half + 1)
        } else {
            word.substring(half, half + 1)
        }
    }
}

/**
 * Find the missing letter
 * https://www.codewars.com/kata/5839edaa6754d6fec10000a2/train/kotlin
 *
 * 'a', 'b', 'c', 'd', 'f'
 */
fun findMissingLetter(array: CharArray): Char {
    if (array.size < 2) throw IllegalArgumentException("array size should be at least 2")

    for(i in 0 .. array.size - 2) {
        if (array[i + 1] - array[i] > 1)
            return array[i] + 1
    }

    // not found, we assume that a character is missing before the first
    return array[0] - 1
}

/**
 * Encrypt this!
 * https://www.codewars.com/kata/5848565e273af816fb000449/train/kotlin
 * The first letter must be converted to its ASCII code.
 * The second letter must be switched with the last letter
 */
fun encryptThis(text: String): String {
    if (text.isBlank()) return ""

    return text.split(" ").map { plain ->
        when (plain.length) {
            1 -> plain[0].code.toString()
            2 -> "${plain[0].code}${plain[1]}"
            else -> "${plain[0].code}${plain[plain.length - 1]}${plain.substring(2, plain.length - 1)}${plain[1]}"
        }
    }.joinToString(" ")
}

/**
 * Character with longest consecutive repetition
 * https://www.codewars.com/kata/586d6cefbcc21eed7a001155/train/kotlin
 * "bbbaaabaaaa", Pair('a',4)
 */
fun longestRepetition(s: String): Pair<Char?,Int> {
    if (s.isEmpty())
        return Pair(null, 0)

    var currentChar = s[0]
    var count = 1
    var maxChar = currentChar
    var maxCount = 1

    for (i in 1 until s.length) {
        if (currentChar == s[i]) { // sequence continues
            count++
        } else { // new sequence
            if (count > maxCount) {
                maxChar = currentChar
                maxCount = count
            }

            currentChar = s[i]
            count = 1
        }
    }

    return if (count > maxCount) Pair(currentChar, count) else Pair(maxChar, maxCount)
}

/**
 * Strip Comments
 * https://www.codewars.com/kata/51c8e37cee245da6b40000bd/train/kotlin
 *
 *NOTE: for speed and lower memory consumption it's better to use state machine to process whole string in one pass.
 */
fun stripComments(input: String, markers: CharArray): String {

    return input.split("\n").joinToString("\n") { str ->
        var candidate = str

        for (m in markers) {
            val substr = str.substringBefore(m)
            if (substr.length < candidate.length)
                candidate = substr
        }

        candidate.trimEnd()
    }
}
