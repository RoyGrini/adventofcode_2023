package day1

import readInput

fun main(args: Array<String>) {
    var input = "day1/input.txt"

    val allCalibNums: ArrayList<Int> = arrayListOf()
    readInput(input)?.forEach { s ->
        val digits = s.filter { it.isDigit() }
        val num = "" + digits.first() + digits.last()
        allCalibNums.add(num.toInt())
//        println("$s  ->  ${allCalibNums.last()}")
    }
    println("Sum unconverted: ${allCalibNums.sum()}")

//    input = "day1/test2.txt"
    allCalibNums.clear()
    readInput(input)?.forEach { s ->
        val digits = s.convertWordDigits()
//        val digits = converted.filter { it.isDigit() }
        val num = "" + digits.first() + digits.last()
        allCalibNums.add(num.toInt())
        println("$s  ->  ${allCalibNums.last()}")
    }
    println("Sum converted: ${allCalibNums.sum()}")
    // Test2 281 + 61 = 342
}

private fun String.convertWordDigits(): String {
//    println(this)
    var result = ""
    var tmp = ""
    run breaking@{
        this.forEach {
            if (it.isLetter()) {
                tmp += it
                tmp = tmp
                    .replace("one", "1")
                    .replace("two", "2")
                    .replace("three", "3")
                    .replace("four", "4")
                    .replace("five", "5")
                    .replace("six", "6")
                    .replace("seven", "7")
                    .replace("eight", "8")
                    .replace("nine", "9")
                if (tmp.last().isDigit()) {
                    result += tmp.last()
                    tmp = ""
                    return@breaking
                }
            } else {
                result += it
                tmp = ""
                return@breaking
            }
//            println("$it - $tmp - $result")
        }
    }

    run breaking@ {
        this.reversed().forEach {
            if (it.isLetter()) {
                tmp = it + tmp
                tmp = tmp
                    .replace("one", "1")
                    .replace("two", "2")
                    .replace("three", "3")
                    .replace("four", "4")
                    .replace("five", "5")
                    .replace("six", "6")
                    .replace("seven", "7")
                    .replace("eight", "8")
                    .replace("nine", "9")
                if (tmp.first().isDigit()) {
                    result += tmp.first()
                    tmp = ""
                    return@breaking
                }
            } else {
                result += it
                tmp = ""
                return@breaking
            }
//            println("$it - $tmp - $result")
        }
    }
    return result
}


