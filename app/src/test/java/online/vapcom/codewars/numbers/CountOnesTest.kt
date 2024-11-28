package online.vapcom.codewars.numbers

import kotlin.test.Test
import kotlin.test.assertEquals

class CountOnesTest {

    @Test
    fun sampleTests() {
        assertEquals(8, countOnes(4, 7))
        assertEquals(7, countOnes(5, 7))
        assertEquals(5, countOnes(12, 29))
    }


    @Test
    fun hexBitsTest() {
        val hexBitsSum = hexBits.sum()
        assertEquals(32, hexBitsSum)
    }

    @Test
    fun bitsSumTest() {
        assertEquals("0, 1, 2, 4, 5, 7, 9, 12, 13, 15, 17, 20, 22, 25, 28, 32", bitsSum.joinToString())
    }

    @Test
    fun simpleBits() {
        assertEquals(0, countBits(0x00L))

        assertEquals(32, countBits(0x0FL))
        assertEquals(65, countBits(0x1FL))
        assertEquals(97, countBits(0x2FL))

        assertEquals(512, countBits(0xFEL))
        assertEquals(516, countBits(0xFFL))

        assertEquals(2577, countBits(0x04FEL))
        assertEquals(2581, countBits(0x04FFL))
        assertEquals(2582, countBits(0x0500L))
    }

}
