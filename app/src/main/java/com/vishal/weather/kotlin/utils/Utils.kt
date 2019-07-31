package com.vishal.weather.kotlin.utils

import kotlin.math.roundToLong


/**
 * This file is a utility class that can be access from anywhere in the application.It contains the
 * commonly used utility methods.
 *
 * @author Vishal - 31st August 2019
 * @since 1.0.0
 */
val DEFAULT_CONNECT_TIMEOUT_IN_MS: Long = 90000
val DEFAULT_WRITE_TIMEOUT_IN_MS: Long = 90000
val DEFAULT_READ_TIMEOUT_IN_MS: Long = 90000
val END_POINT = "http://api.apixu.com/"
const val API_KEY = "key=727b31d5c027441f8e4102934192202"
val FORECAST_DAYS = "7"
private val DEGREE_CHAR = 0x00B0.toChar()
val INPUT_DATE_FORMAT = "yyyy-MM-dd"
val FORECAST_VIEW_DATE_FORMAT = "dd MMM yyyy"

/**
 * Appends a degree symbol to give double value.
 *
 * @param temp vale to be postfixes with degree
 * @return formatted text with degree
 */
fun addDegreeSymbol(temp: Double?): String? {
    return if (null == temp) "- + $DEGREE_CHAR" else temp.roundToLong().toString() + DEGREE_CHAR
}