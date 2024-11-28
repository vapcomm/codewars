package online.vapcom.codewars.algorithms

import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.Test

class NodesLoopTest {

    @Ignore
    @Test
    fun createChainTest() {
        val chain0 = Node.createChain(0, 1)
        printChain(chain0, 0, 1)

        val chain1 = Node.createChain(1, 1)
        printChain(chain1, 1, 1)

        val chain2 = Node.createChain(2, 2)
        printChain(chain2, 2, 2)
    }

    private fun printChain(chain: Node, chainSize: Int, loopSize: Int) {
        print("$chainSize/$loopSize:")
        var node: Node? = chain
        repeat(chainSize) {
            print("${node?.id},")
            node = node?.next
        }
        print("--")
        repeat(loopSize) {
            print("${node?.id},")
            node = node?.next
        }
        println("|")
    }

    @Test
    fun loop_size_of_1() {
        val list = Node.createChain(0, 1)
        val result = loopSize(list)
        assertEquals(1, result)
    }

    @Test
    fun long_tail_short_loop() {
        val list = Node.createChain(8778, 23)
        val result = loopSize(list)
        assertEquals(23, result)
    }

    @Test
    fun short_tail_long_loop() {
        val list = Node.createChain(23, 8778)
        val result = loopSize(list)
        assertEquals(8778, result)
    }

}
