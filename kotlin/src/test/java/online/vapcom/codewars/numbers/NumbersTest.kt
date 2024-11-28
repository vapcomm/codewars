package online.vapcom.codewars.numbers

import org.junit.Assert.*

import org.junit.Test

class NumbersTest {

    @Test
    fun maxMultipleInvalidArgs() {
        assertEquals(0, maxMultiple(0, 7))
        assertEquals(0, maxMultiple(1, 0))
        assertEquals(0, maxMultiple(50, 49))
    }

    @Test
    fun maxMultipleOne() {
        assertEquals(1, maxMultiple(1, 1))
        assertEquals(2, maxMultiple(1, 2))
    }

    @Test
    fun maxMultiple() {
        assertEquals(50, maxMultiple(10, 50))
        assertEquals(6, maxMultiple(2, 7))
        assertEquals(9, maxMultiple(3, 10))
        assertEquals(14, maxMultiple(7, 17))
        assertEquals(185, maxMultiple(37, 200))
        assertEquals(98, maxMultiple(7, 100))
    }
}