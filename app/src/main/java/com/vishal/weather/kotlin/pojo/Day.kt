package com.vishal.weather.kotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Day {

    @SerializedName("maxtemp_c")
    @Expose
    var maxtempC: Double? = null
    @SerializedName("maxtemp_f")
    @Expose
    var maxtempF: Double? = null
    @SerializedName("mintemp_c")
    @Expose
    var mintempC: Double? = null
    @SerializedName("mintemp_f")
    @Expose
    var mintempF: Double? = null
    @SerializedName("avgtemp_c")
    @Expose
    var avgtempC: Double? = null
    @SerializedName("avgtemp_f")
    @Expose
    var avgtempF: Double? = null
    @SerializedName("maxwind_mph")
    @Expose
    var maxwindMph: Double? = null
    @SerializedName("maxwind_kph")
    @Expose
    var maxwindKph: Double? = null
    @SerializedName("totalprecip_mm")
    @Expose
    var totalprecipMm: Double? = null
    @SerializedName("totalprecip_in")
    @Expose
    var totalprecipIn: Double? = null
    @SerializedName("avgvis_km")
    @Expose
    var avgvisKm: Double? = null
    @SerializedName("avgvis_miles")
    @Expose
    var avgvisMiles: Double? = null
    @SerializedName("avghumidity")
    @Expose
    var avghumidity: Double? = null
    @SerializedName("condition")
    @Expose
    var condition: Condition? = null
    @SerializedName("uv")
    @Expose
    var uv: Double? = null

}
