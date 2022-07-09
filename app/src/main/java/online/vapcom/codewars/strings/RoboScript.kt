package online.vapcom.codewars.strings

/**
 * RoboScript #1 - Implement Syntax Highlighting
 *
 * https://www.codewars.com/kata/58708934a44cfccca60000c4/train/kotlin
 * "F3RF5LF7"
 * "FFFR345F2LL"
 * "8(76)RL8F8))92LFR)60193(0887R53(57R"
 * "RRRRRF45L3F2"
 * "RRRRR(F45L3)F2"
 */
fun highlight(code: String): String {
    val regex = Regex("(F+)|(R+)|(L+)|(\\d+)|(\\(+)|(\\)+)")
    return regex.findAll(code).map { mr ->
        val color = when(val char = mr.value.first()) {
            'F' -> "pink"
            'L' -> "red"
            'R' -> "green"
            else -> if (char.isDigit()) "orange" else ""
        }
        if (color.isNotEmpty())
            "<span style=\"color: $color\">${mr.value}</span>"
        else mr.value
    }.joinToString("")
}
