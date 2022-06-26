package online.vapcom.codewars.numbers

import kotlin.math.truncate

// https://www.codewars.com/kata/5aba780a6a176b029800041c/train/kotlin

fun maxMultiple(divisor: Int, bound: Int): Int {
    if (divisor <= 0 || bound <= 0 || divisor > bound)
        return 0

    if (bound % divisor == 0)
        return bound

    var divCount = 1
    while (divCount <= divisor) {
        if ((bound - divCount) % divisor == 0)
            return bound - divCount
        else divCount++
    }

    return 0
}

// Tribonacci Sequence
// https://www.codewars.com/kata/556deca17c58da83c00002db/train/kotlin

fun tribonacci(signature: DoubleArray, n: Int): DoubleArray {
    if (signature.size != 3 || n <= 0)
        return doubleArrayOf()

    if (n <= signature.size)
        return signature.copyOfRange(0, n)

    val result = DoubleArray(n)
    signature.forEachIndexed { index, d -> result[index] = d }

    for (i in signature.size until n) {
        result[i] = result[i - 1] + result[i - 2] + result[i - 3]
    }

    return result
}

// Product of consecutive Fib numbers
// https://www.codewars.com/kata/5541f58a944b85ce6d00006a/train/kotlin
// Есть похожие решения один в один
fun productFib(prod: Long): LongArray {
    var x = 0L
    var y = 1L

    while (x * y < prod) {
        val n = x + y
        x = y
        y = n
    }

    return longArrayOf(x, y, if (x * y == prod) 1 else 0)
}

/**
 * Going to zero or to infinity?
 *
 * https://www.codewars.com/kata/55a29405bc7d2efaff00007c/train/kotlin
 */
fun going(n: Int): Double {

    return when {
        n <= 0 -> 0.0
        n == 1 -> 1.0
        else -> {
            var sum = 1.0
            var mult = 1.0

            for (i in n downTo 2) {
                mult *= i
                sum += 1 / mult
            }

            return truncate(sum * 1E6) / 1E6
        }
    }
}

/**
 * Maximum subarray sum
 *
 * https://www.codewars.com/kata/54521e9ec8e60bc4de000d6c/train/kotlin
 */
fun maxSequence(arr: List<Int>): Int {
    if (arr.isEmpty())
        return 0

    // fast check all negative
    var isAllNegative = true
    for (a in arr) {
        if (a >= 0) {
            isAllNegative = false
            break
        }
    }
    if (isAllNegative) return 0

    fun seqSum(start: Int, end: Int): Int {
        var result = 0
        for (i in start..end) {
            result += arr[i]
        }
        return result
    }

    var maxSum = Int.MIN_VALUE
    for (windowSize in 1..arr.size) {
        for (i in 0..(arr.size - windowSize)) {
            val sum = seqSum(i, i + windowSize - 1)
            if (sum > maxSum)
                maxSum = sum
        }
    }

    return maxSum
}

/**
 * RGB To Hex Conversion
 *
 * https://www.codewars.com/kata/513e08acc600c94f01000001/train/kotlin
 */
fun rgb(r: Int, g: Int, b: Int): String {

    fun clearColor(num: Int): Int {
        return when {
            num < 0 -> 0
            num > 255 -> 255
            else -> num
        }
    }

    return String.format("%02X%02X%02X", clearColor(r), clearColor(g), clearColor(b))
}
