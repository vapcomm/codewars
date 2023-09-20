package online.vapcom.codewars.data

import java.util.Arrays

/**
 * Stack with limited size, old Java code and task for it see below
 */
class Stack(size: Int) {
    private val buf: Array<Any?> = arrayOfNulls(size)
    private var position = 0

    /**
     * Push element to stack
     * @throws OutOfMemoryError if no room for a new element
     */
    fun push(o: Any?) {
        if (position > buf.size - 1) throw OutOfMemoryError()
        buf[position++] = o
    }

    /**
     * Pop element from stack.
     * @return null if stack is empty
     */
    fun pop(): Any? {
        return if (position <= 0) null else buf[--position]
    }

    /**
     * Check stack is empty
     * @return true if it is empty
     */
    val isEmpty: Boolean
        get() = position <= 0 // изменил == на более строгое <=

    /**
     * Compare contains with other stack
     * @return true if both stacks contain equal data in equal positions
     */
    override fun equals(other: Any?): Boolean {
        if (other is Stack) {
            // добавил сравнение данных в стеке
            if (position != other.position) return false
            for (i in 0..position) {
                if (i < buf.size && i < other.buf.size) {
                    if (buf[i] !== other.buf[i]) return false
                } else break
            }
            return true
        } else return false
    }

    /**
     * Clear stack data
     */
    fun clear() {
        position = 0
        Arrays.fill(buf, null)
    }

    override fun toString(): String {
        return "$position:${buf.joinToString()}"
    }

    override fun hashCode(): Int {
        var result = buf.contentHashCode()
        result = 31 * result + position
        return result
    }

}

/*
 Old Java version for a task: find all errors, you may make the code better

/*
      What problems do you see in this code?
*/

public class Stack {
      private final Object[] buf;
      private int            position;

      public Stack(int size) {
            buf = new Object[size];
      }

      public void push(Object o) {
            if (position > buf.length - 1)
                  throw new OutOfMemoryError();

            buf[position++] = o;
      }

      public Object pop() {
            if (position <= 0)
                  return null;

            return buf[--position];
      }

      public boolean isEmpty() {
            return (position == 0);
      }

      public boolean equals(Stack o) {
            return (buf == o.buf);
      }
}
*/
