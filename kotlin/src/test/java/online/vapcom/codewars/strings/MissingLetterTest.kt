package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class MissingLetterTest {
    @Test
    fun exampleTests() {
        assertEquals('e', findMissingLetter(charArrayOf('a', 'b', 'c', 'd', 'f')))
        assertEquals('P', findMissingLetter(charArrayOf('O', 'Q', 'R', 'S')))
    }

    @Test
    fun firstLetterTests() {
        assertEquals('a', findMissingLetter(charArrayOf('b', 'c', 'd')))
        assertEquals('A', findMissingLetter(charArrayOf('B', 'C')))
    }

}