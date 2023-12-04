package day4
import readInput


fun main(args: Array<String>) {
//    val input = "day4/test.txt"
    val input = "day4/input.txt"

    val cardPoints  = mutableMapOf<Int, Int>()
    readInput(input)?.forEach { s ->
        var points = 1;
        val winningNumbers = s.getWinningNumbers(s)
        val yourNumbers = s.getYourNumbers(s)
        val cardNum = s.split(":")[0].split(" ").last().toInt()
        yourNumbers.filter { winningNumbers.contains(it) }.forEach { _ -> points = points shl 1 }
        cardPoints.put(cardNum, points shr 1)
        println("Card $cardNum: ${cardPoints[cardNum]} points")
    }
    println("Sum of points: ${cardPoints.values.sum()}")

    val cardNums  = mutableMapOf<Int, Int>()
    readInput(input)?.forEach { s ->
        val winningNumbers = s.getWinningNumbers(s)
        val yourNumbers = s.getYourNumbers(s)
        val cardNum = s.split(":")[0].split(" ").last().toInt()
        val numCopies = yourNumbers.filter { winningNumbers.contains(it) }.size
        val originalCopies = cardNums.getOrDefault(cardNum,0)

        cardNums[cardNum] = cardNums.getOrDefault(cardNum,0) + 1
        ((cardNum+1)..(cardNum+(numCopies))).forEach { i ->
            cardNums[i] = cardNums.getOrDefault(i,0) + (1 + originalCopies)
        }

        println("$s -> $numCopies plus $originalCopies times")
        ((cardNum)..(cardNum+(numCopies))).forEach { i -> println("\tCard $i: ${cardNums[i]}")}
    }
    cardNums.forEach{ (card, num) -> println("Card $card: $num")}
    println("Sum of cards: ${cardNums.values.sum()}")
}

fun String.getWinningNumbers(s: String): List<Int> {
    val winningNumbers = getNumbers(s.split(":")[1].split("|")[0]);
    return winningNumbers
}

fun String.getYourNumbers(s: String): List<Int> {
    val yourNumbers = getNumbers(s.split(":")[1].split("|")[1]);
    return yourNumbers
}

fun String.getNumbers(s: String): List<Int> {
    val numbers = s.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }.sorted()
    return numbers
}