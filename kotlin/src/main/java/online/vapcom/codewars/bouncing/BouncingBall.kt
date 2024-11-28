package online.vapcom.codewars.bouncing

// https://www.codewars.com/kata/5544c7a5cb454edb3c000047/train/kotlin

/**
 * Three conditions must be met for a valid experiment:
 * Float parameter "h" in meters must be greater than 0
 * Float parameter "bounce" must be greater than 0 and less than 1
 * Float parameter "window" must be less than h.
 */
fun bouncingBall(h:Double, bounce:Double, window:Double):Int {

    return if(h > 0.0 && bounce > 0.0 && bounce < 1.0 && window < h) {
        var result = 1
        var jump = h * bounce

        while (jump > window) {
            jump *= bounce
            result += 2
        }

        return result
    } else -1
}