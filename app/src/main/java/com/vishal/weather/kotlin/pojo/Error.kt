package com.vishal.weather.kotlin.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Error {

    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

}
