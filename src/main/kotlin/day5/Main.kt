package day5
import readInput
import kotlin.math.max
import kotlin.math.min


val seedToSoil = RangeSet()
val soilToFertilizer = RangeSet()
val fertilizerToWater = RangeSet()
val waterToLight = RangeSet()
val lightToTemperature = RangeSet()
val temperatureToHumidity = RangeSet()
var humidityToLocation = RangeSet()
val humidityToLocationSorted = RangeSet();

fun main(args: Array<String>) {
//    val input = "day5/test.txt"
    val input = "day5/input.txt"

    val lines = readInput(input)!!
    val seeds = getSeeds(lines.take(1)[0])
    val seeds2 = getSeeds2(lines.take(1)[0])

    var mapType = MapType.UNKNOWN

    lines.drop(1).forEach { s ->
        println(s)
        if (s.isEmpty()) {
            mapType = MapType.UNKNOWN
            return@forEach
        }
        when (mapType) {
            MapType.UNKNOWN -> mapType = MapType[s.split(" ")[0]]!!

            MapType.SEED_TO_SOIL -> seedToSoil.addRange(Range.getRange(s))
            MapType.SOIL_TO_FERTILIZER -> soilToFertilizer.addRange(Range.getRange(s))
            MapType.FERTILIZER_TO_WATER -> fertilizerToWater.addRange(Range.getRange(s))
            MapType.WATER_TO_LIGHT -> waterToLight.addRange(Range.getRange(s))
            MapType.LIGHT_TO_TEMPERATURE -> lightToTemperature.addRange(Range.getRange(s))
            MapType.TEMPERATURE_TO_HUMIDITY -> temperatureToHumidity.addRange(Range.getRange(s))
            MapType.HUMIDITY_TO_LOCATION -> humidityToLocation.addRange(Range.getRange(s))
        }
    }
    println(seedToSoil.getDestination(98))
    println(seedToSoil.getDestination(14))
    println(seedToSoil.getDestination(79))

    humidityToLocationSorted.addRanges(humidityToLocation.ranges.sortedBy { it.destination }.toList())

    var lowestSeedLoc = Long.MAX_VALUE
    seeds.forEach { seed ->
        lowestSeedLoc = min(lowestSeedLoc, getLocation(seed))
    }
    println("Part 1: lowest seed location: $lowestSeedLoc")

    var location = getLocationAfter(0);
    println("Lowest possible location: $location")
}

fun getLocation(seed: Long): Long {
    return humidityToLocation.getDestination(
        temperatureToHumidity.getDestination(
            lightToTemperature.getDestination(
                waterToLight.getDestination(
                    fertilizerToWater.getDestination(
                        soilToFertilizer.getDestination(
                            seedToSoil.getDestination(seed)
                        )
                    )
                )
            )
        )
    )
}

fun getSeed(location: Long): Long {
    return humidityToLocation.getSource(
        temperatureToHumidity.getSource(
            lightToTemperature.getSource(
                waterToLight.getSource(
                    fertilizerToWater.getSource(
                        soilToFertilizer.getSource(
                            seedToSoil.getSource(location)
                        )
                    )
                )
            )
        )
    )
}

fun getLocationAfter(location: Long) : Long {
    return max(
        humidityToLocationSorted.ranges.stream()
        .filter({ r -> r.destination >= location })
        .findFirst().get().destination,
        location)
}

class RangeSet {
    val ranges = mutableListOf<Range>()

    fun addRange(range: Range) {
        ranges.add(range)
    }

    fun addRanges(ranges: List<Range>) {
        this.ranges.addAll(ranges)
    }


    fun getRangeForDestination(destination: Long) : Range {
        return ranges.filter { r -> destination >= r.destination && destination <= r.destination + r.range }.first()
    }

    fun getDestination(source: Long) : Long {
        ranges.forEach { r ->
            if (source >= r.source && source <= r.source + r.range) {
                return r.destination + (source - r.source)
            }
        }
        return source;
    }

    fun getSource(destination: Long) : Long {
        if (destination < 0) {
            return -1
        }
        ranges.forEach { r ->
            if (destination >= r.destination && destination <= r.destination + r.range) {
                return r.source + (destination - r.destination)
            }
        }
        return -1;
    }
}

data class Range(
    val destination: Long,
    val source: Long,
    val range: Long
) {
    companion object {
        fun getRange(s: String): Range {
            val elems = s.split(" ")
            return Range(elems[0].toLong(), elems[1].toLong(), elems[2].toLong())
        }
    }
}

fun mapRange(range: Range) : Map<Long, Long> {
    val map = mutableMapOf<Long, Long>()
    for (i in 0..range.range-1) {
        map[range.source + i] = range.destination + i
    }
    return map
}

fun getSeeds(s: String) : List<Long> {
    return s.split(":")[1].trim().split(" ").map { it.toLong() }.toList()
}

fun getSeeds2(s: String) : List<Pair<Long, Long>> {
    var seedranges = s.split(":")[1].trim().split(" ").map { it.toLong() }.toList()
    val result = mutableListOf<Pair<Long, Long>>()
    while (seedranges.size > 0) {
        val start = seedranges[0]
        val length = seedranges[1]
        seedranges = seedranges.drop(2)
        result.add(Pair(start, start+length))
    }
    return result
}

fun isSeedInSeeds(seed: Long, seeds: List<Pair<Long, Long>>) : Boolean {
    seeds.forEach { (start, end) ->
        if (seed >= start && seed <= end) {
            return true
        }
    }
    return false
}

enum class MapType(val typeName: String) {
    UNKNOWN("unknown"),
    SEED_TO_SOIL("seed-to-soil"),
    SOIL_TO_FERTILIZER("soil-to-fertilizer"),
    FERTILIZER_TO_WATER("fertilizer-to-water"),
    WATER_TO_LIGHT("water-to-light"),
    LIGHT_TO_TEMPERATURE("light-to-temperature"),
    TEMPERATURE_TO_HUMIDITY("temperature-to-humidity"),
    HUMIDITY_TO_LOCATION("humidity-to-location");

    companion object {
        private val map = entries.associateBy { it.typeName }
        operator fun get(typeName: String) = map[typeName]
    }
}