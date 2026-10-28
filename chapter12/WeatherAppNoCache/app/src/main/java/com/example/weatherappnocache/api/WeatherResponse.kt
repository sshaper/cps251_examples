package com.example.weatherappnocache.api

// A successful JSON response becomes this object and its nested objects/lists.
// Names intentionally match API keys, including snake_case, for Gson mapping.
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

// The coord JSON object contains longitude and latitude.
data class Coord(
    val lon: Double,
    val lat: Double
)

// One entry in the weather JSON array; the UI reads the first description.
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

// With metric units, temperatures are Celsius and pressures are hPa.
// Nullable pressure fields allow these optional values to be absent.
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?
)

// Metric wind speed/gust are m/s; deg is direction in degrees.
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
)

// Cloud coverage is a percentage.
data class Clouds(
    val all: Int
)

// Sunrise/sunset and response dt are Unix timestamps in seconds.
// The screen displays those raw numbers; it does not format local clock times.
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
