package online.vapcom.codewars.sdrow

private const val REVERSE_WORD_LENGTH = 5

fun spinWords(sentence: String): String {
    val split = sentence.split(' ')

    val sb = StringBuilder()
    val last = split.size - 1
    //TODO: см. joinToString
    split.forEachIndexed { index, str ->
        sb.append(
            if(str.length >= REVERSE_WORD_LENGTH) str.reversed()
            else str
        )
        if(index < last) sb.append(' ')
    }

    return sb.toString()
}