package online.vapcom.codewars.strings

/**
 * #26 int32 to IPv4
 * https://www.codewars.com/kata/52e88b39ffb6ac53a400022e/
 */
fun unsignedToIP4Address(a: UInt): String {
    return "${a shr 24}.${(a shr 16) and 0xFFu}.${(a shr 8) and 0xFFu}.${a and 0xFFu}"
}
