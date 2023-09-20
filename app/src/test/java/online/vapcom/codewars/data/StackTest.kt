package online.vapcom.codewars.data

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for Stack class
 */
class StackTest {

    @Test
    fun testPush() {
        val stack = Stack(4)

        assertTrue(stack.isEmpty)

        stack.push("alice")

        assertFalse(stack.isEmpty)

        stack.push(1)
        stack.push(2)
        stack.push(3)

        assertFailsWith<OutOfMemoryError> {
            stack.push("should throw exception")
        }

        assertEquals("4:alice, 1, 2, 3", stack.toString())
    }

    @Test
    fun testPushInEmptyStack() {
        val stack = Stack(0)
        assertTrue(stack.isEmpty)

        assertFailsWith<OutOfMemoryError> {
            stack.push("can't push to empty stack")
        }
        assertTrue(stack.isEmpty)
    }

    @Test
    fun testPopEmpty() {
        val stack = Stack(0)
        assertEquals("0:", stack.toString())

        assertNull(stack.pop())
        assertTrue(stack.isEmpty)
    }

    @Test
    fun testPushPop() {
        val stack = Stack(4)

        assertTrue(stack.isEmpty)

        stack.push("alice")
        assertEquals("1:alice, null, null, null", stack.toString())

        assertEquals("alice", stack.pop())
        assertEquals("0:alice, null, null, null", stack.toString())

        stack.push("bob")
        assertEquals("1:bob, null, null, null", stack.toString())

        stack.push(1)
        assertEquals("2:bob, 1, null, null", stack.toString())

        assertEquals(1, stack.pop())
        assertEquals("1:bob, 1, null, null", stack.toString())

        stack.push(2)
        stack.push(3)

        assertEquals("3:bob, 2, 3, null", stack.toString())

        assertEquals(3, stack.pop())
        assertEquals("2:bob, 2, 3, null", stack.toString())

        assertEquals(2, stack.pop())
        assertEquals("1:bob, 2, 3, null", stack.toString())

        assertEquals("bob", stack.pop())
        assertEquals("0:bob, 2, 3, null", stack.toString())

        assertNull(stack.pop())
        assertTrue(stack.isEmpty)
    }

    @Test
    fun testEquals() {
        val stack1 = Stack(3)
        val stack2 = Stack(2)
        val stack3 = Stack(3)

        // empty stacks are equal
        assertTrue(stack1.equals(stack2))
        assertTrue(stack1.equals(stack3))

        // different values
        stack1.push("alice")
        stack2.push("bob")
        assertFalse(stack1.equals(stack2))

        // equal many values
        stack3.push("alice")
        assertTrue(stack1.equals(stack3))
        stack3.push("bob")
        assertFalse(stack1.equals(stack3))
        stack1.push("bob")
        assertTrue(stack1.equals(stack3))

        assertEquals("2:alice, bob, null", stack1.toString())
        assertEquals("2:alice, bob, null", stack3.toString())

        // stacks with different sizes but equal values
        stack2.clear()
        stack2.push("alice")
        stack2.push("bob")
        assertEquals("2:alice, bob", stack2.toString())
        assertTrue(stack1.equals(stack2))
        assertTrue(stack3.equals(stack2))
    }

}
