package online.vapcom.codewars.strings

// Rainfall
// https://www.codewars.com/kata/56a32dd6e4f4748cc3000006/train/kotlin
// strng = "Rome:Jan 81.2,Feb 63.2,Mar 70.3,Apr 55.7,May 53.0,Jun 36.4,Jul 17.5,Aug 27.5,Sep 60.9,Oct 117.7,Nov 111.0,Dec 97.9" +
//            "\n" +
fun mean(town: String, strng: String): Double {

    strng.split("\n").forEach { str ->
        if (str.substringBefore(":") == town) {
            var sum = 0.0
            var count = 0
            str.split(",").forEach { month ->
                val rainfall = month.substringAfterLast(" ").toDoubleOrNull() ?: -1.0
                if (rainfall >= 0) {
                    sum += rainfall
                    count++
                }
            }

            return if (count == 0) -1.0 else sum/count.toDouble()
        }
    }

    return -1.0
}

fun variance(town: String, strng: String): Double {

    strng.split("\n").forEach { str ->
        if (str.substringBefore(":") == town) {
            println("found town: '$town' in '$str'")
            val months = str.split(",")
            if (months.isEmpty()) return -1.0

            val rainfalls = DoubleArray(months.size)
            var sum = 0.0
            months.forEachIndexed { index, month ->
                val rainfall = month.substringAfterLast(" ").toDoubleOrNull() ?: 0.0
                sum += rainfall
                rainfalls[index] = rainfall
            }

            val mean = sum / rainfalls.size.toDouble()

            var variance = 0.0
            rainfalls.forEach { rf ->
                val s = rf - mean
                variance += s*s
            }

            variance /= rainfalls.size.toDouble()
            return variance
        }
    }

    return -1.0
}