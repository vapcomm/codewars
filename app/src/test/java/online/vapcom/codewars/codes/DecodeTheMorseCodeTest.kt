package online.vapcom.codewars.codes

import org.junit.Assert.*

import org.junit.Test

class DecodeTheMorseCodeTest {

    @Test
    fun emptyCode() {
        assertEquals("", decodeMorse(""))
    }

    @Test
    fun sos() {
        assertEquals("SOS", decodeMorse("...---..."))
    }

    @Test
    fun spaces() {
        assertEquals("HEY JUDE", decodeMorse(" .... . -.--   .--- ..- -.. ."))
        assertEquals("HEY JUDE", decodeMorse("   .... . -.--   .--- ..- -.. ."))
        assertEquals("HEY JUDE", decodeMorse("   .... . -.--   .--- ..- -.. .  "))
    }

    @Test
    fun exampleTestCases() {
        assertEquals("HEY JUDE", decodeMorse(".... . -.--   .--- ..- -.. ."))
    }

    /**
     * Decode the Morse code, advanced
     *
     * https://www.codewars.com/kata/54b72c16cd7f5154e9000457/train/kotlin
     */
    @Test
    fun decodeBitsExampleTestCases() {
        assertEquals("HEY JUDE", decodeMorse(decodeBits("1100110011001100000011000000111111001100111111001111110000000000000011001111110011111100111111000000110011001111110000001111110011001100000011")))

        // testBasicBitsDecoding
        assertEquals("E", decodeMorse(decodeBits("1")))
        assertEquals("I", decodeMorse(decodeBits("101")))
        assertEquals("EE", decodeMorse(decodeBits("10001")))
        assertEquals("A", decodeMorse(decodeBits("10111")))
        assertEquals("M", decodeMorse(decodeBits("1110111")))

        // testLongMessagesHandling
        assertEquals("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.", decodeMorse(decodeBits("00011100010101010001000000011101110101110001010111000101000111010111010001110101110000000111010101000101110100011101110111000101110111000111010000000101011101000111011101110001110101011100000001011101110111000101011100011101110001011101110100010101000000011101110111000101010111000100010111010000000111000101010100010000000101110101000101110001110111010100011101011101110000000111010100011101110111000111011101000101110101110101110")))

        // testMultipleBitsPerDotHandling
        assertEquals("E", decodeMorse(decodeBits("111")))
        assertEquals("E", decodeMorse(decodeBits("1111111")))
        assertEquals("I", decodeMorse(decodeBits("110011")))
        assertEquals("I", decodeMorse(decodeBits("111000111")))
        assertEquals("I", decodeMorse(decodeBits("111110000011111")))
        assertEquals("M", decodeMorse(decodeBits("11111100111111")))

        // testExtraZerosHandling
        assertEquals("E", decodeMorse(decodeBits("01110")))
    }

    @Test
    fun decodeBitsTestCases() {
        assertEquals("", decodeBits(""))
        assertEquals("", decodeBits("0"))
        assertEquals("", decodeBits("00000"))

        assertEquals(".", decodeBits("1"))

        assertEquals(".", decodeBits("11"))
        assertEquals(".", decodeBits("0011"))
        assertEquals("..", decodeBits("110011"))
        assertEquals("..", decodeBits("11001100"))

        assertEquals("...", decodeBits("110011001"))
    }

}
