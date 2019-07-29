package com.vishal.weather.kotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TemperatureResponse {

    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("current")
    @Expose
    var current: Current? = null
    @SerializedName("forecast")
    @Expose
    var forecast: Forecast? = null
    @SerializedName("error")
    @Expose
    var error: Error? = null

}
