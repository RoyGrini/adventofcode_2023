package day2

import readInput
import kotlin.math.max

fun main(args: Array<String>) {
    var input = "day2/input.txt"

    val testMax = mapOf("red" to 12, "blue" to 14, "green" to 13)
    val games = mutableMapOf<Int, Map<String, Int>>()
    val gamePower = mutableMapOf<Int, Int>()
    val possibleGames = ArrayList<Int>()
    readInput(input)?.forEach { s ->
        val (gameNum, result) = parseLine(s)
        if (possible(testMax, result)) {
            possibleGames.add(gameNum)
        }
        gamePower[gameNum] = gamePower(result)
    }
    println("Possible games: $possibleGames")
    println("Possible games sum:" + possibleGames.sum())
    println("Game powers: $gamePower")
    println("Game powers sum:" + gamePower.values.sum())
}

fun parseLine(line: String) : Pair<Int, Map<String, Int>> {
    val result = mutableMapOf<String, Int>()
    val game = line.split(":")
    val gameNum = game[0].split(" ")[1].toInt()
    val draws = game[1].split(";")
    for (draw in draws) {
        val cubes = draw.split(",")
        for (cube in cubes) {
            val (count, color) = cube.trim().split(" ")
            result[color] = max(result.getOrDefault(color, 0), count.toInt())
        }
    }
    return Pair(gameNum, result)
}

fun possible(max: Map<String, Int>, game: Map<String, Int>) : Boolean {
    return max.all { (color, count) -> game.getOrDefault(color, 0) <= count }
}

fun gamePower(game: Map<String, Int>) : Int {
    return game.values.reduce{ acc, i -> acc * i }
}