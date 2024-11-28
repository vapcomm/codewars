package online.vapcom.codewars.algorithms

class Node(val id: Int, var next: Node?) {
    companion object {
        fun createChain(chainSize: Int, loopSize: Int): Node {
            val loop: Array<Node> = if (loopSize == 1) {
                val n = Node(1, null)
                n.next = n
                val a = Array(1) { n }
                a[0] = n
                a
            } else {
                val loop = Array(loopSize) { Node(it + 1, null) }

                for (i in loop.lastIndex - 1  downTo 0) {
                    loop[i].next = loop[i + 1]
                }
                loop[loop.lastIndex].next = loop[0]
                loop
            }

            if (chainSize <= 0)
                return loop[0]

            val chain = Array(chainSize) { Node(it + 1 + 1000000, null) }
            chain[chain.lastIndex].next = loop[0]
            for (i in chain.lastIndex - 1 downTo 0) {
                chain[i].next = chain[i + 1]
            }

            return chain[0]
        }
    }
}

/**
 * #27 Can you get the loop ?
 * https://www.codewars.com/kata/52a89c2ea8ddc5547a000863
 */
fun loopSize(n: Node): Int {
    // find first node in loop
    fun firstNodeInLoop(n: Node): Node {
        val knownNodes = HashSet<Node>()
        var node: Node = n

        while (!knownNodes.contains(node)) {
            knownNodes.add(node)
            node = node.next as Node
        }

        return node
    }

    val firstInLoop = firstNodeInLoop(n)

    if (firstInLoop.next === firstInLoop)
        return 1

    // count loop
    var nodes = 1
    var node = firstInLoop.next
    while (node != firstInLoop) {
        node = node?.next
        nodes ++
    }

    return nodes
}
