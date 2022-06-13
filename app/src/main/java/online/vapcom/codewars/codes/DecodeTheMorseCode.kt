package online.vapcom.codewars.codes

val MorseCode : HashMap<String, String> = hashMapOf(
    "...---..." to "SOS",
    // "HEY JUDE", decodeMorse(".... . -.--   .--- ..- -.. ."
    "...." to "H",
    "." to "E",
    "-.--" to "Y",
    ".---" to "J",
    "..-" to "U",
    "-.." to "D",
)

fun decodeMorse(code: String): String {

    val sb = StringBuilder()
    code.trim().split("   ").forEach { word ->
        word.split(" ").forEach { letter ->
            if(letter.isNotEmpty())
                sb.append(MorseCode[letter] ?: "_")
        }
        sb.append(" ")
    }

    return sb.toString().trim()
}