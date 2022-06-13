package online.vapcom.codewars.strings

/**
 * Molecule to atoms
 * https://www.codewars.com/kata/52f831fa9d332c6591000511/train/kotlin
 */
fun getAtoms(formula: String): Map<String, Int> {
    if (formula.isEmpty())
        throw IllegalArgumentException("Empty formula")

    return getSubformulaAtoms(formula)
}

fun getSubformulaAtoms(formula: String): LinkedHashMap<String, Int> {
    println("sub formula: '${formula}'")

    val result = LinkedHashMap<String, Int>()

    // "S", "He", "PbS"
    // "H20", "B2H6", "C6H12O6", "NaNO3"
    // "Mg(OH)2", "K4[ON(SO3)2]2"

    val len = formula.length
    var i = 0
    var element = ""
    while (i < len) {
        println("formula[$i]: '${formula[i]}'")
        when {
            formula[i].isUpperCase() -> { // start of element name
                if(element.isNotEmpty()) { // it's a next element, add previous one
                    addElement(element, 1, result)
                }

                if (i + 1 < len && formula[i + 1].isLowerCase()) { // second letter of element's name
                    element = formula.substring(i, i + 2)     // two letters name: Pb
                    i += 2
                } else {
                    element = formula.substring(i, i + 1)     // one letter name: P
                    i++
                }
            }

            formula[i].isDigit() -> {   // start of element's atoms number
                if(element.isEmpty()) throw IllegalArgumentException("Empty element before digit at $i char")

                var lastDigitIndex = i + 1
                while (lastDigitIndex < len) {
                    if (formula[lastDigitIndex].isDigit()) lastDigitIndex++
                    else break
                }

                val atomsNumber = formula.substring(i, lastDigitIndex).toInt()
                i = lastDigitIndex
                addElement(element, atomsNumber, result)
                element = ""
            }

            formula[i] == '(' || formula[i] == '[' || formula[i] == '{' -> { // Mg(OH)2, Cu(OH)
                if(element.isNotEmpty()) {
                    addElement(element, 1, result)
                    element = ""
                }

                val closingChar = getClosingChar(formula[i])
                val closing = getClosingIndex(closingChar, formula, i)

                val compoundResult = getSubformulaAtoms(formula.substring(i+1, closing))

                val compoundNumber =  if (closing + 1 < len && formula[closing + 1].isDigit()) { // has number after ()
                    var lastDigitIndex = closing + 1
                    while (lastDigitIndex < len) {
                        if (formula[lastDigitIndex].isDigit()) lastDigitIndex++
                        else break
                    }
                    i = lastDigitIndex
                    formula.substring(closing + 1, lastDigitIndex).toInt()
                } else {
                    i = closing + 1
                    1
                }

                addElements(compoundResult, compoundNumber, result)
            }

            else -> throw IllegalArgumentException("Wrong formula character '${formula[i]}' at index $i")
        }
    }

    if(element.isNotEmpty()) { // the last one element's atom
        addElement(element, 1, result)
    }

    return result
}

fun getClosingIndex(closingChar: Char, formula: String, openIndex: Int): Int {
    // {((H)2)[O]}
    val openChar = formula[openIndex]
    var openCount = 0
    val len = formula.length
    var i = openIndex + 1
    while (i < len) {
        if (formula[i] == openChar) {
            openCount++
        } else if(formula[i] == closingChar) {
            if(openCount <= 0) {
                return i
            } else openCount--
        }

        i++
    }

    throw IllegalArgumentException("No matching '$closingChar' for '${formula[openIndex]}' from index $openIndex")
}

fun getClosingChar(openChar: Char): Char {
    return when(openChar) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        else -> throw IllegalArgumentException("Wrong open character '$openChar'")
    }
}

fun addElement(element: String, atomsNumber: Int, result: LinkedHashMap<String, Int>) {
    result[element] = (result[element] ?: 0) + atomsNumber
}

fun addElements(from: LinkedHashMap<String, Int>, number: Int, result: LinkedHashMap<String, Int>) {
    from.forEach { entry ->
        addElement(entry.key, entry.value * number, result)
    }
}
