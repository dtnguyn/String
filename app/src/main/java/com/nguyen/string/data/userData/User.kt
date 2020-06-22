package com.nguyen.string.data.userData

import com.google.gson.annotations.SerializedName
import com.nguyen.string.data.interestData.Photo

data class User (
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var facebookID: String? = null,
    var profilePhoto: String? = null,
    var gender: String? = null,
    var bio: String? = null,
    var website: String? = null,
    @SerializedName("checkfollow")
    var checkFollow: Boolean? = null,
    var currentLocation: String? =  null,
    var isSuperUser: Boolean? = null,
    var photos: List<Photo>? = null
)