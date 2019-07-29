package com.vishal.weather.kotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Forecast {

    @SerializedName("forecastday")
    @Expose
    var forecastday: List<Forecastday>? = null

}
