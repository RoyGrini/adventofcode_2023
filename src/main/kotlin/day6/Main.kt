package day6

fun main(args: Array<String>) {
    var marginOfError = 1L
//    testRecords.forEach {
//    testRecords2.forEach {
//    records.forEach {
    records2.forEach {
        println("Race record: Time: ${it.time}, Distance: ${it.distance} ")
        val shortest = shortestPress( it.time, it.distance, it.time.floorDiv(2), it.time )
        val longest = longestPress( it.time, it.distance, (it.time - shortest).floorDiv(2) + shortest, it.time )
        println("Shortest: $shortest, Longest: $longest")
        marginOfError *= longest-shortest+1
    }
    println("Margin of error: $marginOfError")
}

fun shortestPress(time: Long, distance : Long, press : Long, bestPress : Long): Long {
    val thisDistance = press * (time-press)
    val win = thisDistance > distance
    println("  SHORT: Press: $press, bestPress: $bestPress, Distance: $thisDistance,       WIN: $win")
    if (win) {
        val nextPress = if (press%2 == 0L) { press.floorDiv(2) } else { (press+1).floorDiv(2) }
        return shortestPress(time, distance, nextPress, press)
    } else {
        if (bestPress-press == 1L) {
            return if (bestPress == time)
                shortestPress(time, distance, bestPress-1, bestPress)
            else
                bestPress
        }
        return shortestPress(time, distance, (bestPress - press).floorDiv(2) + press, bestPress)
    }
}

fun longestPress(time: Long, distance : Long, press : Long, bestPress : Long): Long {
    val thisDistance = press * (time-press)
    val win = thisDistance > distance
    println("  LONG: Press: $press, bestPress: $bestPress, Distance: $thisDistance,       WIN: $win")
    if (win) {
        if ((press - bestPress) in 0..1) {
            return if (bestPress == time)
                longestPress(time, distance, 1 , bestPress)
            else
                press
        }
        val nextPress = (time - press).floorDiv(2) + press + 1 + if ((time - press)%2 == 0L) { 0L } else { 1L }
        return longestPress(time, distance, nextPress, press)
    } else {
        if (bestPress-press == 1L) {
            return if (bestPress == time)
                longestPress(time, distance, 1, bestPress)
            else bestPress
        }
        return longestPress(time, distance, (bestPress - press).floorDiv(2) + press, bestPress)
    }
}

val testRecords = listOf(
    Record(7, 9),
    Record(15, 40),
    Record(30, 200),
)

val testRecords2 = listOf(
    Record(71530, 940200),
)

val records = listOf(
    Record(51, 377),
    Record(69, 1171),
    Record(98, 1224),
    Record(78, 1505),
)

val records2 = listOf(
    Record(51699878, 377117112241504),
)

data class Record(val time: Long, val distance: Long)
