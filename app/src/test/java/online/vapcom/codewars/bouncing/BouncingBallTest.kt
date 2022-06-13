package online.vapcom.codewars.bouncing

import org.junit.Assert.*

import org.junit.Test

class BouncingBallTest {

    @Test
    fun test0() {
        assertEquals(-1, bouncingBall(3.0, 1.0, 1.5))
        assertEquals(-1, bouncingBall(0.0, 0.1, 1.5))
        assertEquals(-1, bouncingBall(-10.0, 0.1, 1.5))

        assertEquals(-1, bouncingBall(3.0, 0.0, 1.5))
        assertEquals(-1, bouncingBall(3.0, 0.0, 3.5))
    }

    @Test
    fun test1() {
        assertEquals(3, bouncingBall(3.0, 0.66, 1.5))
    }

    @Test
    fun test2() {
        assertEquals(15, bouncingBall(30.0, 0.66, 1.5))
    }
}