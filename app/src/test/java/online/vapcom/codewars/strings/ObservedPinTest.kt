package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class ObservedPinTest {
    @Test
    fun sample_tests() {
        setOf(
            "8"   to listOf("5","7","8","9","0"),
            "11"  to listOf("11", "22", "44", "12", "21", "14", "41", "24", "42"),
            "369" to listOf("339","366","399","658","636","258","268","669","668","266","369",
                            "398","256","296","259","368","638","396","238","356","659","639",
                            "666","359","336","299","338","696","269","358","656","698","699","298","236","239")
        ).forEach { assertEquals(it.second.sorted(), getPINs(it.first).sorted(),
            message="Come on, detective! You can do it better! These are not the PIN variations for '${it.first}'") }
    }
}