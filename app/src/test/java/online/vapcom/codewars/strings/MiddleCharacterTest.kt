package online.vapcom.codewars.strings

import org.junit.Assert.*
import org.junit.Test

class MiddleCharacterTest {
    @Test
    fun basicTest() {
        assertEquals("es", getMiddle("test"))
        assertEquals("dd", getMiddle("middle"))
        assertEquals("t", getMiddle("testing"))
        assertEquals("A", getMiddle("A"))
    }

    @Test
    fun bordersTest() {
        assertEquals("", getMiddle(""))
        assertEquals("AZ", getMiddle("AZ"))
        assertEquals("T", getMiddle("ATZ"))
    }
}