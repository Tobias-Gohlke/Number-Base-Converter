import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow

fun convertToDecimal(sourceBase: String, source: Int): BigInteger {
    var result = BigInteger("0")
    val sourceList = sourceBase.toCharArray()
    var exponent = sourceList.size - 1

    for (i in sourceList) {
        result += (if(i.isLetter()) (i.code - 87).toBigInteger() else i.toString().toBigInteger()) * (source.toDouble().pow(exponent--).toBigDecimal()).toBigInteger()
    }
    return result
}

fun convertToTarget(decimal: BigInteger, target: Int): String {
    val ten = BigInteger.TEN
    var remainder = BigInteger("0")
    var decimalResult = decimal
    var result = ""
    val char = 'a'

    while (decimalResult > BigInteger.ZERO) {
        remainder = decimalResult % target.toBigInteger()
        decimalResult /= target.toBigInteger()
        result += if(remainder >= ten) char + (remainder - ten).toInt() else remainder
    }
    return result.reversed()
}

fun fractionalToDecimal(fractionalBase: String, source: Int): BigDecimal {
    var result = BigDecimal("0")
    var exponent = -1

    for (i in fractionalBase) {
        result += ((if(i.isLetter()) (i.code - 87).toBigInteger() else i.toString().toBigInteger()).toBigDecimal() * source.toDouble().pow(exponent--).toBigDecimal()).setScale(5, RoundingMode.HALF_UP)
    }
    return result
}

fun convertToTargetFractional(fractional: BigDecimal, target: Int):String {
    var fractionalDeci = fractional
    var remainder: BigDecimal
    val char = 'a'
    var result = ""

    while (fractionalDeci != BigDecimal.ZERO.setScale(fractionalDeci.scale())) {
        remainder = (fractionalDeci * target.toBigDecimal())
        result += if(remainder.toInt() >= 10) char + (remainder.toInt() - 10) else remainder.toInt()
        fractionalDeci = remainder - remainder.setScale(0, RoundingMode.DOWN)
        if(result.length > 4) break
    }
    while (result.length < 5) {
        result += "0"
    }
    return result
}

fun findResult(source: Int, target: Int) {
    while (true) {
        var finalResult = ""
        println("Enter number in base $source to convert to base $target (To go back type /back)")
        var input = readLine()!!
        var fractionalBase = ""
        var decimalBase = ""
        if (input == "/back") break
        if (input.contains(".")) {
            decimalBase = input.substring(0 until input.indexOf("."))
            fractionalBase = input.substring(input.indexOf(".") + 1)
        } else decimalBase = input

        val decimal = convertToDecimal(decimalBase, source)
        val fractional = fractionalToDecimal(fractionalBase, source)

        val finalResultDeci = convertToTarget(decimal, target)
        val finalResultFrac = convertToTargetFractional(fractional, target)

        if (fractionalBase == "0" || !input.contains(".")) {
            finalResult = finalResultDeci
        } else if (decimalBase == "0") {
            finalResult = "0.$finalResultFrac"
        } else {
            finalResult = "$finalResultDeci.$finalResultFrac"
        }
        println("Conversion result: $finalResult\n")
    }
    println()
}

fun main() {
    while (true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        try {
            var (source, target) = readLine()!!.split(" ")
            findResult(source.toInt(), target.toInt())
        } catch (e: Exception) {
            break
        }
    }
}
