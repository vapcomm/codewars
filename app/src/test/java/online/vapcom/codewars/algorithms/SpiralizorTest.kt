package online.vapcom.codewars.algorithms

import kotlin.test.assertEquals
import kotlin.test.Test

class SpiralizorTest {

    private fun runTest(n: Int,refSol: Array<ByteArray>) {
        val userSol = Spiralizor.spiralize(n).map { it.joinToString() }.joinToString("\n")
        val refrSol = refSol.map { it.joinToString() }.joinToString("\n")
        assertEquals(refrSol,userSol)
    }

    @Test fun `2 Basic Tests`() {
        val solution5x5 = arrayOf(
            byteArrayOf(1, 1, 1, 1, 1),
            byteArrayOf(0, 0, 0, 0, 1),
            byteArrayOf(1, 1, 1, 0, 1),
            byteArrayOf(1, 0, 0, 0, 1),
            byteArrayOf(1, 1, 1, 1, 1)
        )

        val solution8x8 = arrayOf(
            byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            byteArrayOf(0, 0, 0, 0, 0, 0, 0, 1),
            byteArrayOf(1, 1, 1, 1, 1, 1, 0, 1),
            byteArrayOf(1, 0, 0, 0, 0, 1, 0, 1),
            byteArrayOf(1, 0, 1, 0, 0, 1, 0, 1),
            byteArrayOf(1, 0, 1, 1, 1, 1, 0, 1),
            byteArrayOf(1, 0, 0, 0, 0, 0, 0, 1),
            byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1)
        )

        runTest(5,solution5x5)
        runTest(8,solution8x8)
    }

}
