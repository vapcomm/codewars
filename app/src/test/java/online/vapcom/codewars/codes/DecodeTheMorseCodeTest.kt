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

}