package day3

import readInput
import java.lang.Integer.max
import kotlin.math.min

val gearNums = mutableMapOf<Int, MutableList<Int>>()
//val lineLength = 10
    val lineLength = 140

fun main(args: Array<String>) {
//    val input = "day3/test.txt"
    val input = "day3/input.txt"


    val linePartnums = mutableMapOf<Int, List<Int>>()
    val emptyLine = ".".repeat(lineLength)
    var previousLine = emptyLine
    var currentLine = ""
    var nextLine = ""
    var line = 0
    readInput(input)?.forEach { s ->
        currentLine = nextLine
        nextLine = s
        if (currentLine.isNotEmpty()) {
            println(previousLine)
            println(currentLine)
            println(nextLine)

            linePartnums.put(line, getPartNums(line, currentLine, previousLine, nextLine))
            println("line $line: ${linePartnums[line]}\n")
            line++
            previousLine = currentLine
        }
    }
    currentLine = nextLine
    nextLine = emptyLine
    println(previousLine)
    println(currentLine)
    println(nextLine)

    linePartnums.put(line, getPartNums(line, currentLine, previousLine, nextLine))
    println("line $line: ${linePartnums[line]}")

    println("$gearNums")

    println("Partnum SUM: ${linePartnums.values.flatten().sum()}\n")
    var gearRatioSum = 0
    gearNums.filter{ it.value.size == 2}.forEach { gearRatioSum += it.value[0] * it.value[1] }
    println("Gear ratio SUM: $gearRatioSum")
}

private fun getPartNums(line: Int, currentLine: String, previousLine: String, nextLine: String) : List<Int> {
    val partNums = mutableListOf<Int>()
    var pos = 0
    var num = ""
    currentLine.forEach { char ->
        if (char.isDigit()) {
            num += char
        } else {
            if (num.length > 0 && isPartNum(pos - num.length, pos, previousLine, currentLine, nextLine)) {
                partNums.add(num.toInt())
                val gearPos = gearNum(line, pos - num.length, pos, previousLine, currentLine, nextLine)
                println("Gear pos for line $line")
                if (gearPos >= 0) {
                    if (!gearNums.containsKey(gearPos)) {
                        gearNums.put(gearPos, mutableListOf())
                    }
                    gearNums[gearPos]?.add(num.toInt())
                }
            }
            num = ""
        }
        pos++
    }
    if (num.length > 0 && isPartNum(pos - num.length, pos, previousLine, currentLine, nextLine)) {
        partNums.add(num.toInt())
        val gearPos = gearNum(line, pos - num.length, pos, previousLine, currentLine, nextLine)
        if (gearPos >= 0) {
            if (!gearNums.containsKey(gearPos)) {
                gearNums.put(gearPos, mutableListOf())
            }
            gearNums[gearPos]?.add(num.toInt())
        }
    }
    return partNums
}

fun gearNum(line: Int, start: Int, end: Int, previousLine: String, currentLine: String, nextLine: String): Int {
    val fromIdx = max(0, start - 1)
    val toIdx = min(end + 1, currentLine.length - 1)
    if (previousLine.substring(fromIdx, toIdx).contains("*")) {
        return previousLine.substring(fromIdx, toIdx).indexOf("*") + fromIdx + line*lineLength - lineLength
    }
    if (currentLine.substring(fromIdx, toIdx).contains("*")) {
        return currentLine.substring(fromIdx, toIdx).indexOf("*") + fromIdx + line*lineLength
    }
    if (nextLine.substring(fromIdx, toIdx).contains("*")) {
        return nextLine.substring(fromIdx, toIdx).indexOf("*") + fromIdx + line*lineLength + lineLength
    }
    return -1
}

fun isPartNum(start: Int, end: Int, previousLine: String, currentLine: String, nextLine: String): Boolean {
    if (start > 0 && currentLine[start - 1] != '.') {
        return true
    }
    if (end < currentLine.length - 1 && currentLine[end] != '.') {
        return true
    }
    val fromIdx = max(0, start - 1)
    val toIdx = min(end + 1, currentLine.length - 1)
    return previousLine.substring(fromIdx, toIdx).hasPartMarker() || nextLine.substring(fromIdx, toIdx).hasPartMarker()
}

private fun String.hasPartMarker(): Boolean {
    return  Regex("[\\.0-9 ]").replace(this, "").isNotEmpty()
}

private fun String.hasGearMarker(): Boolean {
    return this.contains("*")
}



