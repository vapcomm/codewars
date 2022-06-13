package online.vapcom.codewars.numbers

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
    if(signature.size != 3 || n <= 0)
        return doubleArrayOf()

    if(n <= signature.size)
        return signature.copyOfRange(0, n)

    val result = DoubleArray(n)
    signature.forEachIndexed { index, d -> result[index] = d }

    for (i in signature.size until n) {
        result[i] = result[i-1] + result[i-2] + result[i-3]
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

    return longArrayOf(x, y, if(x * y == prod) 1 else 0)
}
