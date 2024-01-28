package online.vapcom.codewars.strings

import kotlin.test.assertEquals
import kotlin.test.Test

class IP4AddressTest {

    @Test
    fun exampleTest() {
        assertEquals("128.114.17.104", unsignedToIP4Address(2154959208u))
        assertEquals("0.0.0.0", unsignedToIP4Address(0u))
        assertEquals("128.32.10.1", unsignedToIP4Address(2149583361u))
    }

    @Test
    fun moreTest() {
        assertEquals("127.0.0.1", unsignedToIP4Address(2130706433u))
    }

}
