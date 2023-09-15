package online.vapcom.codewars.codes

val MORSE_CODE : HashMap<String, String> = hashMapOf(
    "...---..." to "SOS",
   	".-" to "A", "-..." to "B",	"-.-." to "C", "-.." to "D",
  	"." to "E", "..-." to "F", "--." to "G", "...." to "H",
  	".." to "I", ".---" to "J", "-.-" to "K", ".-.." to "L",
  	"--" to "M", "-." to "N", "---" to "O",	".--." to "P",
 	"--.-" to "Q", ".-." to "R", "..." to "S", "-" to "T",
 	"..-" to "U", "...-" to "V", ".--" to "W", "-..-" to "X",
 	"-.--" to "Y", "--.." to "Z",
    ".-.-.-" to "."
)

fun decodeMorse(code: String): String {

    val sb = StringBuilder()
    code.trim().split("   ").forEach { word ->
        word.split(" ").forEach { letter ->
            if(letter.isNotEmpty())
                sb.append(MORSE_CODE[letter] ?: "_")
        }
        sb.append(" ")
    }

    return sb.toString().trim()
}

/**
 * Decode the Morse code, advanced
 *
 * https://www.codewars.com/kata/54b72c16cd7f5154e9000457/train/kotlin
 *
 * Decode string of 0s and 1s to Morse code
 *```
 * 1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011
 * ···· · −·−−   ·−−− ··− −·· ·
 *```
 * "Dot" – is 1 time unit long.
 * "Dash" – is 3 time units long.
 * Pause between dots and dashes in a character – is 1 time unit long.
 * Pause between characters inside a word – is 3 time units long.
 * Pause between words – is 7 time units long.
 *
 * Note that some extra 0's may naturally occur at the beginning and the end of a message, make sure to ignore them.
 * Also if you have trouble discerning if the particular sequence of 1's is a dot or a dash, assume it's a dot.
 */
fun decodeBits(bits: String): String {

    // return number of zeros from offset
    fun skipZeros(bits: String, off: Int): Int {
        var i = off
        while (i < bits.length && bits[i] == '0') i++

        return i - off
    }

    // return string with spaces which length is based on zeros' number
    fun spacesFromZeros(zeros: Int, timeUnit: Int): String {
        return when {
            // pause between characters
            timeUnit * 2 < zeros && zeros < timeUnit * 7 -> " "
            // pause between words
            zeros >= timeUnit * 7 -> "   "
            else -> ""
        }
    }

    // return minimum '1' sequence length, i.e. time unit
    fun findTimeUnit(bits: String): Int {
        val trimmedBits = bits.trim('0')
        if (trimmedBits.isEmpty()) return 0

        var currentChar = trimmedBits[0]
        var sequenceLength = 0
        var minSequence = Int.MAX_VALUE

        trimmedBits.forEach { c ->
            if (c == currentChar) {
                sequenceLength++
            } else {
                if (sequenceLength < minSequence)
                    minSequence = sequenceLength

                if (minSequence <= 1) return 1      // it's a minimum possible time unit

                currentChar = c
                sequenceLength = 1
            }
        }

        // last sequence in bits
        if (sequenceLength < minSequence)
            minSequence = sequenceLength

        return minSequence
    }

    // println("-- bits: '$bits'")

    val timeUnit = findTimeUnit(bits)

    if (timeUnit <= 0) return ""

    val sb = StringBuilder()
    var n = skipZeros(bits, 0)
    var state = BitsDecoderState.DOT
    var symbolLength = 0

    while (n < bits.length) {
        symbolLength++

        when (state) {
            BitsDecoderState.DOT -> {
                if (bits[n] == '1') {
                    if (symbolLength > timeUnit * 2)
                        state = BitsDecoderState.DASH

                    n++
                } else {
                    sb.append('.')
                    symbolLength = 0

                    val zeros = skipZeros(bits, n)
                    val spaces = spacesFromZeros(zeros, timeUnit)
                    if (spaces.isNotEmpty()) sb.append(spaces)
                    n += zeros
                }
            }
            BitsDecoderState.DASH -> {
                if (bits[n] == '1') {
                    n++
                } else {
                    if (symbolLength >= timeUnit * 3) sb.append('-')
                    else sb.append('.')

                    symbolLength = 0
                    state = BitsDecoderState.DOT

                    val zeros = skipZeros(bits, n)
                    val spaces = spacesFromZeros(zeros, timeUnit)
                    if (spaces.isNotEmpty()) sb.append(spaces)
                    n += zeros
                }
            }
        }
    }

    // possible last symbol in bits
    if (symbolLength >= timeUnit * 3) sb.append('-')
    else if (symbolLength >= 1) sb.append('.')

    // println("-- result: '$sb'")

    return sb.toString()
}


enum class BitsDecoderState {
    DOT, DASH
}
