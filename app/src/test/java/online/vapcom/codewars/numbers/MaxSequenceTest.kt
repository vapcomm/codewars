package online.vapcom.codewars.numbers

import org.junit.Test
import kotlin.test.assertEquals

class MaxSequenceTest {

    @Test
    fun `it should work on an empty list`() {
        assertEquals(0, maxSequence(emptyList()))
    }

    @Test
    fun `it should work on the example`() {
        assertEquals(6, maxSequence(listOf(-2, 1, -3, 4, -1, 2, 1, -5, 4)))
    }

    @Test
    fun `all negative numbers is zero result`() {
        assertEquals(0, maxSequence(listOf(-2, -1, -3)))
    }

    @Test
    fun `one positive number`() {
        assertEquals(3, maxSequence(listOf(3)))
    }

}
