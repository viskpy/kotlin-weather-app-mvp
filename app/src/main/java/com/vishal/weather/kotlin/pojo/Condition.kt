package com.vishal.weather.kotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Condition {

    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("code")
    @Expose
    var code: Int? = null

}
