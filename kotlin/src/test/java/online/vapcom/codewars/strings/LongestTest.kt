package online.vapcom.codewars.strings

import org.junit.Assert.*
import org.junit.Test

class LongestTest {

    @Test
    fun empty() {
        assertEquals("", longest("", ""))
    }

    @Test
    fun test() {
        assertEquals("aehrsty", longest("aretheyhere", "yestheyarehere"))
        assertEquals("abcdefghilnoprstu", longest("loopingisfunbutdangerous", "lessdangerousthancoding"))
        assertEquals("acefghilmnoprstuy", longest("inmanylanguages", "theresapairoffunctions"))
    }

}