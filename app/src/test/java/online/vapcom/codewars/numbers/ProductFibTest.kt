package online.vapcom.codewars.numbers

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class ProductFibTest {

    @Test
    fun simple() {
        assertArrayEquals(longArrayOf(0, 1, 1), productFib(0))
        assertArrayEquals(longArrayOf(1, 1, 1), productFib(1))
        assertArrayEquals(longArrayOf(1, 2, 1), productFib(2))
        assertArrayEquals(longArrayOf(2, 3, 0), productFib(3))
    }

    @Test
    fun codewarsExample() {
        assertArrayEquals(longArrayOf(55, 89, 1), productFib(4895))
        assertArrayEquals(longArrayOf(89, 144, 0), productFib(5895))

        assertArrayEquals(longArrayOf(21, 34, 1), productFib(714))
        assertArrayEquals(longArrayOf(34, 55, 0), productFib(800))
    }

}