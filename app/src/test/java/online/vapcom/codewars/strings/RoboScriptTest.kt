package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class RoboScriptTest {

    @Test
    fun descriptionExamples() {
        assertCorrect("F3RF5LF7", "<span style=\"color: pink\">F</span><span style=\"color: orange\">3</span><span style=\"color: green\">R</span><span style=\"color: pink\">F</span><span style=\"color: orange\">5</span><span style=\"color: red\">L</span><span style=\"color: pink\">F</span><span style=\"color: orange\">7</span>")
        assertCorrect("FFFR345F2LL", "<span style=\"color: pink\">FFF</span><span style=\"color: green\">R</span><span style=\"color: orange\">345</span><span style=\"color: pink\">F</span><span style=\"color: orange\">2</span><span style=\"color: red\">LL</span>")
    }

    private fun assertCorrect(code: String, expected: String) {
        println("Code without syntax highlighting:   $code")
        val actual = highlight(code)
        println("Your code with syntax highlighting: $actual")
        println("Expected syntax highlighting:       $expected")
        assertEquals(expected, actual, "Code: $code")
    }

    @Test
    fun sampleTests() {
        assertPathEquals("", "*")
        assertPathEquals("FFFFF", "******")
        assertPathEquals("FFFFFLFFFFFLFFFFFLFFFFFL",
            "******\r\n" +
                    "*    *\r\n" +
                    "*    *\r\n" +
                    "*    *\r\n" +
                    "*    *\r\n" +
                    "******")
        assertPathEquals("LFFFFFRFFFRFFFRFFFFFFF", "    ****\r\n    *  *\r\n    *  *\r\n********\r\n    *   \r\n    *   ")
        assertPathEquals("LF5RF3RF3RF7", "    ****\r\n    *  *\r\n    *  *\r\n********\r\n    *   \r\n    *   ")
    }

    @Test
    fun sampleAttemptTests() {
        assertPathEquals("FFFLLFFFFFFRRFFFLFFFRRFFFFFFFF",
        "   *   \r\n" +
                "   *   \r\n" +
                "   *   \r\n" +
                "*******\r\n" +
                "   *   \r\n" +
                "   *   \r\n" +
                "   *   \r\n" +
                "   *   \r\n" +
                "   *   ")
        assertPathEquals("RFR3F1R6FLL2LF",
        " * \r\n" +
                "***")
    }

    private fun assertPathEquals(code: String, expected: String) {
        val actual = execute(code)
        assertEquals(expected, actual, "--------------\nCode: $code\nYou returned:\n$actual\nExpected path of robot:\n$expected\n--------------\n")
    }
}
