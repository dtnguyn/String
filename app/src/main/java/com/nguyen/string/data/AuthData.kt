package com.nguyen.string.data

import com.google.gson.annotations.SerializedName


data class AuthData (
    var id: Int? = null,
    var username: String? = "",
    var email: String? = "",
    @SerializedName("access_token")
    var accessToken: String? = ""
)