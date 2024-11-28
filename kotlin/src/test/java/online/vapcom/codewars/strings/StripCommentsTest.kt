package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class StripCommentsTest {

    @Test
    fun empty() {
        assertEquals("", stripComments("", charArrayOf('#')))
        assertEquals("", stripComments(" ", charArrayOf('#')))
        assertEquals("", stripComments("   ", charArrayOf('#')))
        assertEquals("\n", stripComments(" \n  ", charArrayOf('#')))
    }

    @Test
    fun kataExample() {
        assertEquals("apples, pears\n" +
                "grapes\n" +
                "bananas",
            stripComments("apples, pears # and bananas\ngrapes\nbananas !apples", charArrayOf('#', '!')))
    }

    @Test
    fun testFixed() {
        assertEquals("apples, plums\n" +
                "pears\n" +
                "oranges",
            stripComments("apples, plums % and bananas\n" +
                "pears\n" +
                "oranges !applesauce", charArrayOf('%', '!')))
        assertEquals("Q\nu\ne", stripComments("Q @b\nu\ne -e f g", charArrayOf('@', '-')))
    }

    @Test
    fun markersOrder() {
        assertEquals("apples, pears\n" +
                "grapes\n" +
                "bananas",
            stripComments("apples, pears # and ! bananas\ngrapes\nbananas !apples", charArrayOf('!', '#')))
    }
}
