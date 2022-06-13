package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class LongestRepetitionTest {
    private fun runTest(s: String,sol: Pair<Char?,Int>) = assertEquals(sol, longestRepetition(s))

    @Test fun `Example tests`() {
        runTest("bbbaaabaaaa", Pair('a',4))
        runTest("aaaabb", Pair('a',4))
        runTest("cbdeuuu900", Pair('u',3))
        runTest("abbbbb", Pair('b',5))
        runTest("aabb", Pair('a',2))
        runTest("", Pair(null,0))
        runTest("ba", Pair('b',1))
        runTest("b", Pair('b',1))
    }
}